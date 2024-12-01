package com.example.csc207courseproject.data_access;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import com.example.csc207courseproject.use_case.select_event.SelectEventDataAccessInterface;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseDataAccessInterface;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import okhttp3.*;

/**
 * A class responsible for making all API calls to start.gg.
 */
public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MutateSeedingDataAccessInterface,
        SelectTournamentDataAccessInterface, ReportSetDataAccessInterface, SelectEventDataAccessInterface {

    private static final String API_URL = "https://api.start.gg/gql/alpha";
    private String token = BuildConfig.TOKEN;
    private Map<Integer, Integer> idToSeedID = new HashMap<>();
    private int initialPhaseID;
    private List<Integer> overallSeeding;
    private JSONObject jsonResponse;
    private CountDownLatch countDownLatch;

    /**
     * Makes an API call to start.gg with the given query.
     * @param query the query for the API call
     */
    private void sendRequest(String query) {
        countDownLatch = new CountDownLatch(1);
        final OkHttpClient client = new OkHttpClient();
        final MediaType mediaType = MediaType.parse("application/json");

        final RequestBody body = RequestBody.create(query, mediaType);
        final Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                throw new RuntimeException(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    final String r = response.body().string();
                    jsonResponse = new JSONObject(r);
                    countDownLatch.countDown();
                }
                catch (IOException | JSONException evt) {
                    throw new RuntimeException(evt);
                }

            }

        });
        try {
            countDownLatch.await();
        }
        catch (InterruptedException evt) {
            throw new RuntimeException(evt);
        }

    }

    /**
     * Gets all the upcoming tournaments that the current user is organizing or is an admin for.
     */
    @Override
    public List<List> getTournaments() throws JSONException {

        final String q = "query getCurrentUser($page: Int!, $perPage: Int!) { currentUser { id "
                + "tournaments(query: { filter: {upcoming: true} page: $page, perPage: $perPage }) "
                + "{ nodes { id name admins { id }} } } }";

        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"page\": 1, \"perPage\": 10 } }";

        sendRequest(json);
        final JSONObject currUser;
        try {
            currUser = jsonResponse.getJSONObject("data")
                    .getJSONObject("currentUser");
            jsonResponse = null;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }

        final int userID = currUser.getInt("id");
        final JSONArray allTournaments = currUser.getJSONObject("tournaments").getJSONArray("nodes");

        // Filter out the tournaments not organized by the user
        final List<String> userTournamentNames = new ArrayList<>();
        final List<Integer> userTournamentIds = new ArrayList<>();
        for (int i = 0; i < allTournaments.length(); i++) {
            final JSONObject tournament = allTournaments.getJSONObject(i);
            final Object admins = tournament.get("admins");

            if (admins == JSONObject.NULL) {
                continue;
            }
            final JSONArray adminsArray = (JSONArray) admins;
            for (int j = 0; j < adminsArray.length(); j++) {
                final JSONObject admin = adminsArray.getJSONObject(j);
                if (admin.getInt("id") == userID) {
                    userTournamentNames.add(tournament.getString("name"));
                    userTournamentIds.add(tournament.getInt("id"));
                    break;
                }
            }
        }

        final List<List> userTournaments = new ArrayList<>();
        userTournaments.add(userTournamentNames);
        userTournaments.add(userTournamentIds);
        return userTournaments;
    }

    /**
     * Gets the events in the given tournament.
     * @param tournamentID The id of the tournament.
     * @return The events by their name and id.
     */
    public List<List> getEventsInTournament(Integer tournamentID) throws JSONException {

        final String q = "query getEvents($id: ID) { tournament(id: $id) { events { id name } } }";

        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"id\": \"" + tournamentID + "\"}}";

        sendRequest(json);
        final JSONArray events;
        try {
            events = jsonResponse.getJSONObject("data")
                    .getJSONObject("tournament")
                    .getJSONArray("events");
            jsonResponse = null;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }

        final List<String> eventNames = new ArrayList<>();
        final List<Integer> eventIds = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            final JSONObject event = events.getJSONObject(i);
            eventNames.add(event.getString("name"));
            eventIds.add(event.getInt("id"));
        }
        final List<List> eventsList = new ArrayList<>();
        eventsList.add(eventNames);
        eventsList.add(eventIds);
        return eventsList;
    }

    /**
     * Gets the event id of a given event.
     * @param eventLink The link of the event
     * @return The id of the event
     */
    public int getEventId(String eventLink) {

        final String q = "query getEventId($slug: String) {event(slug: $slug) {id name}}";

        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"slug\": \"" + eventLink + "\"}}";

        sendRequest(json);
        try {
            final int eventId = jsonResponse.getJSONObject("data").getJSONObject("event").getInt("id");
            jsonResponse = null;
            return eventId;
        }
        catch (JSONException evt) {
            throw new RuntimeException(evt);
        }
    }

    /**
     * Gets all the event data required by the EventData entity for the given event.
     * @param eventID The ID of the event.
     * @return A list containing entrants (index 0), participants (index 1), characters (index 2),
     *      and phase IDs (index 3).
     */
    public List<Object> getEventData(Integer eventID) {
        // Create query
        final String q = "query getEventData($id: ID!) { event(id: $id) { entrants(query: {page: 1, perPage: 64}) "
                + "{ nodes { id participants { id prefix gamerTag user { id } } } } phases { id name } videogame "
                + "{ characters { id name } } } }";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"id\": \"" + eventID + "\"}}";
        sendRequest(json);

        final List<Object> eventData = new ArrayList<>();
        eventData.addAll(getEntrantsAndParticipants());
        eventData.add(getCharacters());
        eventData.add(getPhaseIds());

        jsonResponse = null;
        return eventData;
    }

    /**
     * Extract entrants and participants from json response.
     * @return Maps of entrants and participants (entrants at 0 and participants at 1)
     */
    private List<Object> getEntrantsAndParticipants() {
        try {
            final JSONArray jsonEntrants = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("entrants").getJSONArray("nodes");

            // Create entrants and participants maps and fill it in
            final Map<Integer, Entrant> entrantsMap = new HashMap<>();
            final Map<Integer, Participant> participantsMap = new HashMap<>();

            for (int i = 0; i < jsonEntrants.length(); i++) {
                final JSONObject entrantObject = jsonEntrants.getJSONObject(i);
                final JSONArray jsonParticipants = entrantObject.getJSONArray("participants");
                final int entrantId = entrantObject.getInt("id");

                final Participant[] participants = new Participant[jsonParticipants.length()];

                // Fill in the names and user ids for each player on a team
                for (int j = 0; j < jsonParticipants.length(); j++) {
                    final int participantId = jsonParticipants.getJSONObject(j).getInt("id");
                    final String participantName = jsonParticipants.getJSONObject(j).getString("gamerTag");
                    int userId;
                    // Ensure user isn't null
                    try {
                        userId = jsonParticipants.getJSONObject(j).getJSONObject("user").getInt("id");
                    }
                    catch (JSONException evt) {
                        userId = -1;
                    }
                    String participantSponsor = "";

                    // Ensure sponsor isn't null string
                    if (!jsonParticipants.getJSONObject(j).getString("prefix").equals("null")) {
                        participantSponsor = jsonParticipants.getJSONObject(j).getString("prefix");
                    }
                    final Participant participant =
                            new Participant(participantId, userId, participantName, participantSponsor);
                    participantsMap.put(participantId, participant);
                    participants[j] = participant;
                }

                final Entrant entrant = new Entrant(participants, entrantId);
                entrantsMap.put(entrantId, entrant);
            }
            final List<Object> entrantsAndParticipants = new ArrayList<>();
            entrantsAndParticipants.add(entrantsMap);
            entrantsAndParticipants.add(participantsMap);

            return entrantsAndParticipants;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Extract characters from json response.
     * @return A sorted map of character names to character IDs.
     */
    private SortedMap<String, Integer> getCharacters() {
        try {
            final JSONObject videogame = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("videogame");

            final SortedMap<String, Integer> characters = new TreeMap<>();
            if (videogame.get("characters") == JSONObject.NULL) {
                return characters;
            }
            else {
                final JSONArray charactersArray = videogame.getJSONArray("characters");
                for (int i = 0; i < charactersArray.length(); i++) {
                    final JSONObject characterObject = charactersArray.getJSONObject(i);
                    final String characterName = characterObject.getString("name");
                    final int characterId = characterObject.getInt("id");
                    characters.put(characterName, characterId);
                }
                return characters;
            }
        }
        catch (JSONException evt) {
            throw new RuntimeException(evt);
        }
    }

    /**
     * Extract phase IDs from json response.
     * @return A sorted map of phase names to phase IDs.
     */
    private SortedMap<String, Integer> getPhaseIds() {
        try {
            final JSONArray jsonPhases = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONArray("phases");

            // Save initial phase for seeding data
            initialPhaseID = jsonPhases.getJSONObject(0).getInt("id");

            // Create name to id map and fill it in
            final SortedMap<String, Integer> nameToID = new TreeMap<>();

            for (int i = 0; i < jsonPhases.length(); i++) {
                final int phaseID = jsonPhases.getJSONObject(i).getInt("id");
                final String phaseName = jsonPhases.getJSONObject(i).getString("name");
                nameToID.put(phaseName, phaseID);
            }
            return nameToID;
        }
        catch (JSONException evt) {
            throw new RuntimeException(evt);
        }
    }

    private void createOverallSeeding() {
        // Create query
        final String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)"
                + "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}"
                + "nodes {seedNum id entrant {id}}}}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + initialPhaseID
                + "\", \"page\": 1, \"perPage\": 64}}";

        sendRequest(json);

        try {
            final JSONArray jsonSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes");
            jsonResponse = null;

            // Create list of seeds and fill it in seeded order
            final List<Integer> seeding = new ArrayList<>();

            for (int i = 0; i < jsonSeeds.length(); i++) {
                final int id = jsonSeeds.getJSONObject(i).getJSONObject("entrant").getInt("id");
                idToSeedID.put(id, jsonSeeds.getJSONObject(i).getInt("id"));
                seeding.add(id);
            }
            overallSeeding = seeding;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Gets the seeding for the given phase.
     * @param phaseID The ID of the phase
     * @return A list of player IDs in seeded order
     */
    @Override
    public List<Integer> getSeedingInPhase(int phaseID) {
        if (overallSeeding == null) {
            createOverallSeeding();
        }
        // Create query for the number of entrants in the phase
        final String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)"
                + "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}"
                + "nodes {seedNum}}}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + phaseID
                + "\", \"page\": 1, \"perPage\": 64}}";

        sendRequest(json);

        try {
            final int numSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes").length();
            jsonResponse = null;

            // Return seeding sliced to include only this phase
            return overallSeeding.subList(0, numSeeds);
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Mutates the seeding on start gg to match the parameter seeding.
     * @param seededEntrants A list in seeded order of player IDs for each entrant
     */
    @Override
    public void setSeeding(List<Integer> seededEntrants) {
        // Fill in unmodified seeds with new values
        for (int i = 0; i < seededEntrants.size(); i++) {
            overallSeeding.set(i, seededEntrants.get(i));
        }

        try {
            // Generate seed mapping
            final JSONArray seedMapping = new JSONArray();
            for (int i = 0; i < overallSeeding.size(); i++) {
                final JSONObject seedMap = new JSONObject();
                seedMap.put("seedId", idToSeedID.get(overallSeeding.get(i)));
                seedMap.put("seedNum", i + 1);
                seedMapping.put(seedMap);
            }

            // Create query
            final String q = "mutation UpdatePhaseSeeding ($phaseId: ID!, $seedMapping: [UpdatePhaseSeedInfo]!)"
                    + "{updatePhaseSeeding (phaseId: $phaseId, seedMapping: $seedMapping) {id}}";

            final JSONObject json = new JSONObject();
            final JSONObject variables = new JSONObject();
            variables.put("phaseId", initialPhaseID);
            variables.put("seedMapping", seedMapping);
            json.put("query", q);
            json.put("variables", variables);

            sendRequest(json.toString());
            jsonResponse = null;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Sets the value of the token attribute.
     * @param token the value to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void reportSet(int setID, int winnerID, List<Map<String, Integer>> gameData) {

    }
}
