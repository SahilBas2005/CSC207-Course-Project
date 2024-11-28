package com.example.csc207courseproject.data_access;

import android.util.Log;
import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.use_case.select_event.SelectEventDataAccessInterface;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseDataAccessInterface;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MutateSeedingDataAccessInterface,
        SelectTournamentDataAccessInterface, ReportSetDataAccessInterface, SelectEventDataAccessInterface {

    private String TOKEN = BuildConfig.TOKEN;
    private final String API_URL = "https://api.start.gg/gql/alpha";
    private Map<Integer, Integer> idToSeedID = new HashMap<>();
    private int initialPhaseID;
    private List<Integer> overallSeeding;
    private JSONObject jsonResponse;
    private CountDownLatch countDownLatch;

    private void sendRequest(String query) {
        countDownLatch = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(query, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                throw new RuntimeException(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String r = response.body().string();
                    jsonResponse = new JSONObject(r);
                    Log.d("api_call", jsonResponse.toString());
                    countDownLatch.countDown();
                } catch(IOException | JSONException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Get all the upcoming tournaments that the current user is organizing.
     */
    @Override
    public List<List> getTournaments() throws JSONException {

        String q = "query getCurrentUser($page: Int!, $perPage: Int!) { currentUser { id " +
                "tournaments(query: { filter: {upcoming: true} page: $page, perPage: $perPage }) { nodes { id name admins { id }} } } }";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"page\": 1, \"perPage\": 10 } }";

        sendRequest(json);
        JSONObject currUser;
        try {
            currUser = jsonResponse.getJSONObject("data")
                    .getJSONObject("currentUser");
            jsonResponse = null;
        } catch (JSONException event) {
            throw new RuntimeException(event);
        }

        int userID = currUser.getInt("id");
        JSONArray allTournaments = currUser.getJSONObject("tournaments").getJSONArray("nodes");

        // Filter out the tournaments not organized by the user
        List<String> userTournamentNames = new ArrayList<>();
        List<Integer> userTournamentIDs = new ArrayList<>();
        for (int i = 0; i < allTournaments.length(); i++) {
            JSONObject tournament = allTournaments.getJSONObject(i);
            Object admins = tournament.get("admins");

            if (admins == JSONObject.NULL) {
                continue;
            }
            JSONArray adminsArray = (JSONArray) admins;
            for (int j = 0; j < adminsArray.length(); j++) {
                JSONObject admin = adminsArray.getJSONObject(j);
                if (admin.getInt("id") == userID) {
                    userTournamentNames.add(tournament.getString("name"));
                    userTournamentIDs.add(tournament.getInt("id"));
                    break;
                }
            }
        }

        List<List> userTournaments = new ArrayList<>();
        userTournaments.add(userTournamentNames);
        userTournaments.add(userTournamentIDs);
        return userTournaments;
    }

    /**
     * Get the events in the given tournament.
     * @param tournamentID The id of the tournament.
     * @return The events by their name and id.
     */
    public List<List> getEventsInTournament(Integer tournamentID) throws JSONException {

        String q = "query getEvents($id: ID) { tournament(id: $id) { events { id name } } }";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"id\": \"" + tournamentID + "\"}}";

        sendRequest(json);
        JSONArray events;
        try {
            events = jsonResponse.getJSONObject("data")
                    .getJSONObject("tournament")
                    .getJSONArray("events");
            jsonResponse = null;
        } catch (JSONException event) {
            throw new RuntimeException(event);
        }

        List<String> eventNames = new ArrayList<>();
        List<Integer> eventIDs = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            eventNames.add(event.getString("name"));
            eventIDs.add(event.getInt("id"));
        }
        List<List> eventsList = new ArrayList<>();
        eventsList.add(eventNames);
        eventsList.add(eventIDs);
        return eventsList;
    }

    /**
     * Gets the event id of a given event.
     * @param eventLink The link of the event
     * @return The id of the event
     */
    public int getEventId(String eventLink) {

        String q = "query getEventId($slug: String) {event(slug: $slug) {id name}}";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"slug\": \"" + eventLink + "\"}}";

        sendRequest(json);
        try{
            int eventId = jsonResponse.getJSONObject("data").getJSONObject("event").getInt("id");
            jsonResponse = null;
            return eventId;
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all the event data required by the EventData entity for the given event.
     * @param eventID The ID of the event.
     * @return A list containing entrants (index 0), participants (index 1), whether there are characters (index 2),
     * and phase IDs (index 3).
     */
    public List<Object> getEventData(Integer eventID) {
        // Create query
        String q = "query getEventData($id: ID!) { event(id: $id) { entrants(query: {page: 1, perPage: 64}) " +
                "{ nodes { id participants { id prefix gamerTag user { id } } } } phases { id name } videogame " +
                "{ characters { id name } } } }";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"id\": \"" + eventID + "\"}}";
        sendRequest(json);

        List<Object> eventData = new ArrayList<>();
        eventData.addAll(getEntrantsAndParticipants());
        eventData.add(checkCharacters());
        eventData.add(getPhaseIDs());


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
            Map<Integer, Entrant> entrantsMap = new HashMap<>();
            Map<Integer, Participant> participantsMap = new HashMap<>();

            for (int i = 0; i < jsonEntrants.length(); i++) {
                JSONObject entrantObject = jsonEntrants.getJSONObject(i);
                JSONArray jsonParticipants = entrantObject.getJSONArray("participants");
                int entrantId = entrantObject.getInt("id");

                Participant[] participants = new Participant[jsonParticipants.length()];

                // Fill in the names and user ids for each player on a team
                for (int j = 0; j < jsonParticipants.length(); j++) {
                    int participantId = jsonParticipants.getJSONObject(j).getInt("id");
                    String participantName = jsonParticipants.getJSONObject(j).getString("gamerTag");
                    int userId;
                    // Ensure user isn't null
                    try {
                        userId = jsonParticipants.getJSONObject(j).getJSONObject("user").getInt("id");
                    }
                    catch(JSONException e) {
                        userId = -1;
                    }
                    String participantSponsor = "";

                    // Ensure sponsor isn't null string
                    if (!jsonParticipants.getJSONObject(j).getString("prefix").equals("null")){
                        participantSponsor = jsonParticipants.getJSONObject(j).getString("prefix");
                    }
                    Participant participant = new Participant(participantId, userId, participantName, participantSponsor);
                    participantsMap.put(participantId, participant);
                    participants[j] = participant;
                }

                Entrant entrant = new Entrant(participants, entrantId);
                entrantsMap.put(entrantId, entrant);
            }
            List<Object> entrantsAndParticipants = new ArrayList<>();
            entrantsAndParticipants.add(entrantsMap);
            entrantsAndParticipants.add(participantsMap);

            return entrantsAndParticipants;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Check if the event has characters from json response.
     * @return true if the event has characters; false otherwise
     */
    private boolean checkCharacters() {
        try {
            final JSONObject videogame = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("videogame");
            if (videogame.has("characters")) {
                Object characters = videogame.get("characters");
                if (characters instanceof JSONArray) {
                    return true;
                }
            }
            return false;
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract phase IDs from json response
     * @return A sorted map of all the phase IDs mapped to their name.
     */
    private SortedMap<String, Integer> getPhaseIDs() {
        try{
            final JSONArray jsonPhases = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONArray("phases");

            // Save initial phase for seeding data
            initialPhaseID = jsonPhases.getJSONObject(0).getInt("id");

            // Create id to name map and fill it in
            SortedMap<String, Integer> nameToID = new TreeMap<>();

            for (int i = 0; i < jsonPhases.length(); i++) {
                int phaseID = jsonPhases.getJSONObject(i).getInt("id");
                String phaseName = jsonPhases.getJSONObject(i).getString("name");
                nameToID.put(phaseName, phaseID);
            }
            return nameToID;
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void createOverallSeeding(){
        // Create query
        String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)" +
                "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}" +
                "nodes {seedNum id entrant {id}}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + initialPhaseID + "\", \"page\": 1, \"perPage\": 64}}";

        sendRequest(json);

        try {
            final JSONArray jsonSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes");
            jsonResponse = null;

            // Create list of seeds and fill it in seeded order
            List<Integer> seeding = new ArrayList<>();

            for (int i = 0; i < jsonSeeds.length(); i++) {
                int id = jsonSeeds.getJSONObject(i).getJSONObject("entrant").getInt("id");
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
        String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)" +
                "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}" +
                "nodes {seedNum}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + phaseID + "\", \"page\": 1, \"perPage\": 64}}";

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
        for(int i = 0; i < seededEntrants.size(); i++) {
            overallSeeding.set(i, seededEntrants.get(i));
        }

        try {
            // Generate seed mapping
            JSONArray seedMapping = new JSONArray();
            for(int i = 0; i < overallSeeding.size(); i++) {
                JSONObject seedMap = new JSONObject();
                seedMap.put("seedId", idToSeedID.get(overallSeeding.get(i)));
                seedMap.put("seedNum", i + 1);
                seedMapping.put(seedMap);
            }

            // Create query
            String q = "mutation UpdatePhaseSeeding ($phaseId: ID!, $seedMapping: [UpdatePhaseSeedInfo]!)" +
                    "{updatePhaseSeeding (phaseId: $phaseId, seedMapping: $seedMapping) {id}}";


            JSONObject json = new JSONObject();
            JSONObject variables = new JSONObject();
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

    public void setTOKEN(String token) {
        this.TOKEN = token;
    }

    @Override
    public void reportSet(int setID, int winnerID, List<Map<String, Integer>> gameData) {

    }
}
