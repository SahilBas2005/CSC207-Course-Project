package com.example.csc207courseproject.data_access.api;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.example.csc207courseproject.entities.*;
import com.example.csc207courseproject.use_case.add_station.AddStationDataAccessInterface;
import com.example.csc207courseproject.use_case.call_set.CallSetDataAccessInterface;
import com.example.csc207courseproject.use_case.get_finance.GetFinanceDataAccessInterface;
import com.example.csc207courseproject.use_case.get_stations.GetStationsDataAccessInterface;
import com.example.csc207courseproject.use_case.login.LoginDataAccessInterface;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import com.example.csc207courseproject.use_case.ongoing_sets.OngoingSetsDataAccessInterface;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import com.example.csc207courseproject.use_case.select_event.SelectEventDataAccessInterface;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseDataAccessInterface;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import com.example.csc207courseproject.use_case.upcoming_sets.UpcomingSetsDataAccessInterface;
import okhttp3.*;

/**
 * The DAO for making API calls to start.gg.
 */
public class APIDataAccessObject implements SelectPhaseDataAccessInterface, LoginDataAccessInterface,
        MutateSeedingDataAccessInterface, ReportSetDataAccessInterface, UpcomingSetsDataAccessInterface,
        OngoingSetsDataAccessInterface, GetStationsDataAccessInterface, AddStationDataAccessInterface,
        CallSetDataAccessInterface, SelectTournamentDataAccessInterface, SelectEventDataAccessInterface, 
        GetFinanceDataAccessInterface {

    private static final String API_URL = "https://api.start.gg/gql/alpha";
    private String token;
    private Map<Integer, Integer> idToSeedID = new HashMap<>();
    private int initialPhaseID;
    private List<Entrant> overallSeeding;
    private JSONObject jsonResponse;
    private CountDownLatch countDownLatch;

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
                throw new APIDataAccessException(jsonResponse.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    final String r = response.body().string();
                    jsonResponse = new JSONObject(r);
                    countDownLatch.countDown();
                }
                catch (IOException | JSONException evt) {
                    throw new APIDataAccessException(jsonResponse.toString());
                }

            }

        });
        try {
            countDownLatch.await();
            Log.d("API Response", jsonResponse.toString());
        }
        catch (InterruptedException evt) {
            throw new APIDataAccessException(jsonResponse.toString());
        }

    }

    /**
     * Get all the upcoming tournaments that the current user is organizing.
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
        final List<Integer> userTournamentIDs = new ArrayList<>();
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
                    userTournamentIDs.add(tournament.getInt("id"));
                    break;
                }
            }
        }

        final List<List> userTournaments = new ArrayList<>();
        userTournaments.add(userTournamentNames);
        userTournaments.add(userTournamentIDs);
        return userTournaments;
    }

    /**
     * Get the events in the given tournament.
     *
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
        final List<Integer> eventIDs = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            final JSONObject event = events.getJSONObject(i);
            eventNames.add(event.getString("name"));
            eventIDs.add(event.getInt("id"));
        }
        final List<List> eventsList = new ArrayList<>();
        eventsList.add(eventNames);
        eventsList.add(eventIDs);
        return eventsList;
    }

    /**
     * Get all the event data required by the EventData entity for the given event.
     *
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

        final List<Object> eventData = new ArrayList<>(getEntrantsAndParticipants());
        eventData.add(getCharacters());
        eventData.add(getPhaseIDs());

        jsonResponse = null;
        return eventData;
    }

    /**
     * Extract entrants and participants from json response.
     *
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
                    final Participant participant = new Participant(participantId, userId,
                            participantName, participantSponsor);
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
     *
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
     *
     * @return A sorted map of phase names to phase IDs.
     */
    private SortedMap<String, Integer> getPhaseIDs() {
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
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + initialPhaseID + "\", "
                + "\"page\": 1, \"perPage\": 64}}";

        sendRequest(json);

        try {
            final JSONArray jsonSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes");
            jsonResponse = null;

            // Create list of seeds and fill it in seeded order
            final List<Entrant> seeding = new ArrayList<>();

            for (int i = 0; i < jsonSeeds.length(); i++) {
                final int id = jsonSeeds.getJSONObject(i).getJSONObject("entrant").getInt("id");
                idToSeedID.put(id, jsonSeeds.getJSONObject(i).getInt("id"));
                seeding.add(EventData.getEventData().getEntrant(id));
            }
            overallSeeding = seeding;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Gets the seeding for the given phase.
     *
     * @param phaseID The ID of the phase
     * @return A list of entrants in seeded order
     */
    @Override
    public List<Entrant> getSeedingInPhase(int phaseID) {
        if (overallSeeding == null) {
            createOverallSeeding();
        }
        // Create query for the number of entrants in the phase
        final String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)"
                + "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}"
                + "nodes {seedNum}}}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + phaseID + "\", "
                + "\"page\": 1, \"perPage\": 64}}";

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
     *
     * @param seededEntrants A list in seeded order of player IDs for each entrant
     */
    @Override
    public void setSeeding(List<Entrant> seededEntrants) {
        // Fill in unmodified seeds with new values
        for (int i = 0; i < seededEntrants.size(); i++) {
            overallSeeding.set(i, seededEntrants.get(i));
        }

        try {
            // Generate seed mapping
            final JSONArray seedMapping = new JSONArray();
            for (int i = 0; i < overallSeeding.size(); i++) {
                final JSONObject seedMap = new JSONObject();
                seedMap.put("seedId", idToSeedID.get(overallSeeding.get(i).getId()));
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

    @Override
    public void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ, int p1EntrantID, int p2EntrantID) {
        try {

            // Initialize and add the parameters that don't need data manipulation

            final JSONObject json = new JSONObject();
            final JSONObject variables = new JSONObject();
            variables.put("setId", setID);
            variables.put("winnerId", winnerId);

            final String q;

            if (hasDQ) {
                // Create query without gameData parameter, as there is no game data due to the DQ
                q = "mutation reportSet($setId: ID!, $winnerId: ID!, $isDQ: Boolean) {"
                        + "reportBracketSet(setId: $setId, winnerId: $winnerId, isDQ: $isDQ){state}}";
                variables.put("isDQ", true);
            }
            else {
                // Create the JSON object for the game data
                final JSONArray gameData = new JSONArray();

                for (int i = 0; i < games.size(); i++) {
                    final JSONObject game = new JSONObject();
                    final Game currGame = games.get(i);
                    game.put("winnerId", currGame.getWinnerID());
                    game.put("gameNum", i + 1);

                    final boolean p1CharSelected = !Objects.equals(currGame.getPlayer1Character(), "No Character");
                    final boolean p2CharSelected = !Objects.equals(currGame.getPlayer2Character(), "No Character");

                    if (p1CharSelected || p2CharSelected) {
                        final JSONArray characterSelections = new JSONArray();

                        if (p1CharSelected) {
                            final JSONObject p1SelectionInput = new JSONObject();
                            final int p1CharacterID = EventData.getEventData().getCharacterIds()
                                    .get(currGame.getPlayer1Character());
                            p1SelectionInput.put("entrantId", p1EntrantID);
                            p1SelectionInput.put("characterId", p1CharacterID);
                            characterSelections.put(p1SelectionInput);
                        }

                        if (p2CharSelected) {
                            final JSONObject p2SelectionInput = new JSONObject();
                            final int p2CharacterID = EventData.getEventData().getCharacterIds()
                                    .get(currGame.getPlayer2Character());
                            p2SelectionInput.put("entrantId ", p2EntrantID);
                            p2SelectionInput.put("characterId", p2CharacterID);
                            characterSelections.put(p2SelectionInput);
                        }
                        game.put("selections", characterSelections);
                    }

                    gameData.put(game);
                }

                // Create query including the gameData parameter
                q = "mutation reportSet($setId: ID!, $winnerId: ID!"
                        + ", $gameData: [BracketSetGameDataInput]) {"
                        + "reportBracketSet(setId: $setId, winnerId: $winnerId, gameData: $gameData){state}}";
                variables.put("gameData", gameData);
            }

            json.put("query", q);
            json.put("variables", variables);
            sendRequest(json.toString());
            jsonResponse = null;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    public List<ReportSetData> getOngoingSets(int eventID) {
        // Sorts them in reverse starting order of start time

        try {

            // Create query
            final String q = "query EventSets($eventId: ID!, $page: Int!, $perPage: Int!) {\n"
                    + "  event(id: $eventId) {"
                    + "    sets("
                    + "      page: $page"
                    + "      perPage: $perPage"
                    + "      sortType: RECENT"
                    + "      filters: {state: [2]}"
                    + "    ) {"
                    + "      pageInfo {"
                    + "        total"
                    + "      }"
                    + "      nodes {"
                    + "        id"
                    + "        totalGames"
                    + "        slots {"
                    + "          entrant {"
                    + "            id"
                    + "            name"
                    + "          }"
                    + "        }"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            // convert to string if this doens't work
            final JSONObject json = new JSONObject();
            final JSONObject variables = new JSONObject();
            variables.put("eventId", eventID);
            variables.put("page", 1);
            variables.put("perPage", 10);
            json.put("query", q);
            json.put("variables", variables);

            sendRequest(json.toString());

        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }

        try {
            final JSONArray jsonSets = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("sets").getJSONArray("nodes");
            jsonResponse = null;

            // Create list of SetData and fill it in the specified order of the API call
            final List<ReportSetData> sets = new ArrayList<>();

            for (int i = 0; i < jsonSets.length(); i++) {
                final int setID = jsonSets.getJSONObject(i).getInt("id");
                final int bestOf = jsonSets.getJSONObject(i).getInt("totalGames");

                final JSONArray slots = jsonSets.getJSONObject(i).getJSONArray("slots");
                final Entrant[] players = new Entrant[slots.length()];

                // Store the players in Entrant Objects and exporting that
                for (int j = 0; j < slots.length(); j++) {
                    final int newId = slots.getJSONObject(j).getJSONObject("entrant").getInt("id");
                    players[j] = EventData.getEventData().getEntrant(newId);
                }

                final ReportSetData newSet = new ReportSetData(setID, players, bestOf);

                sets.add(newSet);
            }
            Collections.reverse(sets);
            return sets;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    public List<CallSetData> getUpcomingSets(int eventID) {
        // Sorts them in callable order

        try {

            // Create query
            final String q = "query EventSets($eventId: ID!, $page: Int!, $perPage: Int!) {\n"
                    + "  event(id: $eventId) {"
                    + "    sets("
                    + "      page: $page"
                    + "      perPage: $perPage"
                    + "      sortType: CALL_ORDER"
                    + "      filters: {state: [1], hideEmpty: true}"
                    + "    ) {"
                    + "      pageInfo {"
                    + "        total"
                    + "      }"
                    + "      nodes {"
                    + "        id"
                    + "        slots {"
                    + "          entrant {"
                    + "            id"
                    + "            name"
                    + "          }"
                    + "        }"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            // convert to string if this doesn't work
            final JSONObject json = new JSONObject();
            final JSONObject variables = new JSONObject();
            variables.put("eventId", eventID);
            variables.put("page", 1);
            variables.put("perPage", 10);
            json.put("query", q);
            json.put("variables", variables);

            sendRequest(json.toString());

        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }

        try {
            final JSONArray jsonSets = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("sets").getJSONArray("nodes");
            jsonResponse = null;

            // Create list of SetData and fill it in the specified order of the API call
            final List<CallSetData> sets = new ArrayList<>();

            // Check if the existing sets are in preview status, where the set ids will be strings with "preview"
            // in them. If they are, call the fixPreview function which will update all of them
            // to the proper integers
            if (jsonSets.length() > 0) {
                try {
                    jsonSets.getJSONObject(0).getInt("id");
                }
                catch (JSONException event) {
                    fixPreview(jsonSets.getJSONObject(0).getString("id"));
                    // return empty to give the API a chance to update before new calls are made
                    return sets;
                }
            }

            for (int i = 0; i < jsonSets.length(); i++) {
                boolean participantNull = false;
                final int setID = jsonSets.getJSONObject(i).getInt("id");

                final JSONArray slots = jsonSets.getJSONObject(i).getJSONArray("slots");
                final Entrant[] players = new Entrant[slots.length()];

                // Store the players in Entrant Objects and exporting that
                for (int j = 0; j < slots.length(); j++) {
                    try {
                        final int newId = slots.getJSONObject(j).getJSONObject("entrant").getInt("id");
                        players[j] = EventData.getEventData().getEntrant(newId);
                    }
                    catch (JSONException event) {
                        participantNull = true;
                    }
                }
                if (!participantNull) {
                    final CallSetData newSet = new CallSetData(setID, players);

                    sets.add(newSet);
                }
            }
            return sets;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Returns the stations assigned to an event.
     *
     * @param eventId The ID of the event
     * @return The stations in a list
     */
    @Override
    public List<Station> getStations(int eventId) {
        // Create query
        final String q = "query EventStations($eventId: ID!, $page: Int!, $perPage: Int!) {event(id: $eventId)"
                + "{stations(query: {page: $page perPage: $perPage}) { pageInfo{total totalPages}"
                + "nodes {id number}}}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"eventId\": \"" + eventId
                + "\", \"page\": 1, \"perPage\": 10}}";

        sendRequest(json);
        try {
            final JSONArray jsonStations = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("stations").getJSONArray("nodes");
            jsonResponse = null;

            // Save stations to a list
            final List<Station> stations = new ArrayList<>();
            for (int i = 0; i < jsonStations.length(); i++) {
                final int id = jsonStations.getJSONObject(i).getInt("id");
                if (EventData.getEventData().getStations().containsKey(id)) {
                    stations.add(EventData.getEventData().getStations().get(id));
                }
                else {
                    final int number = jsonStations.getJSONObject(i).getInt("number");
                    final Station newStation = new Station(id, number);
                    EventData.getEventData().getStations().put(id, newStation);
                    stations.add(newStation);
                }

            }

            return stations;

        }
        catch (JSONException evt) {
            throw new RuntimeException(evt);
        }
    }

    /**
     * Adds a station to an event.
     * @param tournamentId The ID of the event
     * @param stationNumber The number of the station
     * @return The id of the added station
     */
    public int addStation(int tournamentId, int stationNumber) {
        try {

            // Create station input
            final JSONObject fields = new JSONObject();
            fields.put("number", stationNumber);

            // Create query
            final String q = "mutation AddStation($tournamentId: ID, $fields: StationUpsertInput!) {"
                    + "upsertStation(tournamentId: $tournamentId, fields: $fields){id}}";

            final JSONObject json = new JSONObject();
            final JSONObject variables = new JSONObject();
            variables.put("tournamentId", tournamentId);
            variables.put("fields", fields);
            json.put("query", q);
            json.put("variables", variables);

            sendRequest(json.toString());
            final int id = jsonResponse.getJSONObject("data").getJSONObject("upsertStation").getInt("id");
            jsonResponse = null;

            return id;

        }
        catch (JSONException evt) {
            throw new RuntimeException(evt);
        }
    }

    /**
     * Marks the parameter set as in progress.
     *
     * @param setId Set id of the set
     */
    public void callSet(int setId) {
        // Create query
        final String q = "mutation CallSet($setId: ID!) {markSetInProgress(setId: $setId){state}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"setId\": \"" + setId + "\"}}";

        sendRequest(json);
        jsonResponse = null;
    }

    public void fixPreview(String tempId) {
        // Create query
        final String q = "mutation FixPreview($setId: ID!) {reportBracketSet(setId: $setId){id}}";
        final String json = "{ \"query\": \"" + q + "\", \"variables\": { \"setId\": \"" + tempId + "\"}}";

        sendRequest(json);
        jsonResponse = null;
    }

    /**
     * Sets the value of the token attribute.
     * @param token the value to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void fetchParticipantPaymentStatus(int tournamentId) {
        // Create query

        final String q = "query GetTournament($id: ID!, $participantQuery: ParticipantPaginationQuery!) {"
                + "tournament(id: $id) {"
                + "id name participants(query: $participantQuery, isAdmin: true) {"
                + "pageInfo {total perPage page} "
                + "nodes {id}}}}";

        final String json = "{ \"query\": \"" + q + "\", "
                + "\"variables\": { "
                + "\"id\": \"" + tournamentId + "\", "
                + "\"participantQuery\": { "
                + "\"page\": 1, "
                + "\"perPage\": 64, "
                + "\"filter\": { \"unpaid\": false } "
                + "} "
                + "} }";

        // Assuming sendRequest populates jsonResponse
        sendRequest(json);

        try {
            final JSONArray jsonParticipants = jsonResponse.getJSONObject("data")
                    .getJSONObject("tournament")
                    .getJSONObject("participants")
                    .getJSONArray("nodes");

            for (int i = 0; i < jsonParticipants.length(); i++) {
                final JSONObject participantObject = jsonParticipants.getJSONObject(i);

                // Assuming "verified" means "paid"
                final int participantId = participantObject.getInt("id");

                EventData.getEventData().getParticipant(participantId).markAsPaid();
            }
        }
        catch (JSONException evt) {
            evt.printStackTrace();
            throw new RuntimeException("Failed to parse participant JSON response", evt);
        }
        catch (Exception evt) {
            // generic exception
            evt.printStackTrace();

        }
    }
}

