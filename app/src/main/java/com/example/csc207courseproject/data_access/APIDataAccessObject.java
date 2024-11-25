package com.example.csc207courseproject.data_access;

import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.entities.*;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.csc207courseproject.use_case.main.MainDataAccessInterface;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseDataAccessInterface;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MainDataAccessInterface,
        MutateSeedingDataAccessInterface, ReportSetDataAccessInterface {

    private final String TOKEN = BuildConfig.token;
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
     * Gets all entrants at an event using the start gg api.
     * @param eventID The ID of the event
     * @return Maps of entrants and participants (entrants at 0 and participants at 1)
     */
    @Override
    public Object[] getEntrantsandParticipantsInEvent(int eventID) {
        // Create query
        String q = "query EventEntrants($eventId: ID!, $page: Int!, $perPage: Int!) {event(id: $eventId)" +
                "{entrants(query: {page: $page perPage: $perPage}) { pageInfo{total totalPages}" +
                "nodes {id participants {id prefix gamerTag user {id}}}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"eventId\": \"" + eventID + "\", \"page\": 1, \"perPage\": 64}}";

        sendRequest(json);
        try {
            final JSONArray jsonEntrants = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("entrants").getJSONArray("nodes");
            jsonResponse = null;

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
            return new Object[]{entrantsMap, participantsMap};
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    /**
     * Gets all phase IDs for a given event.
     * @param eventID The ID of the event
     * @return A map of all the phase IDs mapped to their name
     */
    @Override
    public SortedMap<String, Integer> getPhaseIDs(int eventID) {
        // Create query
        String q = "query EventPhases($eventId: ID!) {event(id: $eventId)" +
                "{phases {id name}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"eventId\": \"" + eventID + "\"}}";

        sendRequest(json);
        try{
            final JSONArray jsonPhases = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONArray("phases");
            jsonResponse = null;

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

    @Override
    public void reportSet(int setID, int winnerId, List<Game> games, boolean hasDQ) {
        //IMPLEMENT THIS SO THAT IT CONVERTS THE GAME DATA INTO A json ARRAY FOR THE API CALL

        try {

            // Initialize and add the parameters that don't need data manipulation

            JSONObject json = new JSONObject();
            JSONObject variables = new JSONObject();
            variables.put("setId", setID);
            variables.put("winnerId", winnerId);
            variables.put("isDQ", hasDQ);

            String q;

            if (hasDQ) {
                // Create query without gameData parameter, as there is no game data due to the DQ
                q = "mutation reportSet($setId: ID!, $winnerId: ID!, $isDQ: Boolean) {" +
                        "reportBracketSet(setId: $setId, winnerId: $winnerId, isDQ: $isDQ)";
            } else {
                // Create the JSON object for the game data
                JSONArray gameData = new JSONArray();

                for(int i = 0; i < games.size(); i++) {
                    JSONObject game = new JSONObject();
                    Game currGame = games.get(i);
                    game.put("winnerId", currGame.getWinnerID());
                    game.put("gameNum", i + 1);
                    gameData.put(game);
                }

                //STILL NEED TO ADD CHARACTER INFO TO API CALL IF POSSIBLE

                // Create query including the gameData parameter
                q = "mutation reportSet($setId: ID!, $winnerId: ID!, $isDQ: " +
                        "Boolean, $gameData: [BracketSetGameDataInput]) {" +
                        "reportBracketSet(setId: $setId, winnerId: $winnerId, isDQ: $isDQ, gameData: $gameData)";
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


    public List<SetData> getOngoingSets(int eventID) {
        //Sorts them in reverse starting order of start time

        try {

            // Create query
            String q = "query EventSets($eventId: ID!, $page: Int!, $perPage: Int!) {\n" +
                    "  event(id: $eventId) {" +
                    "    sets(" +
                    "      page: $page" +
                    "      perPage: $perPage" +
                    "      sortType: RECENT" +
                    "      filters: {state: [2]}" +
                    "    ) {" +
                    "      pageInfo {" +
                    "        total" +
                    "      }" +
                    "      nodes {" +
                    "        id" +
                    "        totalGames" +
                    "        slots {" +
                    "          id" +
                    "          entrant {" +
                    "            id" +
                    "            name" +
                    "          }" +
                    "        }" +
                    "      }" +
                    "    }" +
                    "  }" +
                    "}";

            //convert to string if this doens't work
            JSONObject json = new JSONObject();
            JSONObject variables = new JSONObject();
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
            List<SetData> sets = new ArrayList<>();

            for (int i = 0; i < jsonSets.length(); i++) {
                int setID = jsonSets.getJSONObject(i).getInt("id");
                int bestOf = jsonSets.getJSONObject(i).getInt("totalGames");

                JSONArray slots = jsonSets.getJSONObject(i).getJSONArray("slots");
                Entrant[] players = new Entrant[slots.length()];

                //Store the players in Entrant Objects and exporting that
                for (int j = 0; j < slots.length(); j++) {
                    int newId = slots.getJSONObject(i).getInt("id");
                    players[i] = EventData.getEntrant(newId);
                }

                SetData newSet = new SetData(setID, players, bestOf);

                sets.add(newSet);
            }
            return sets;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }


    }

    public List<SetData> getUpcomingSets(int eventID) {
        // Sorts them in callable order

        try {

            // Create query
            String q = "query EventSets($eventId: ID!, $page: Int!, $perPage: Int!) {\n" +
                    "  event(id: $eventId) {" +
                    "    sets(" +
                    "      page: $page" +
                    "      perPage: $perPage" +
                    "      sortType: CALL_ORDER" +
                    "      filters: {state: [1]}" +
                    "    ) {" +
                    "      pageInfo {" +
                    "        total" +
                    "      }" +
                    "      nodes {" +
                    "        id" +
                    "        totalGames" +
                    "        slots {" +
                    "          id" +
                    "          entrant {" +
                    "            id" +
                    "            name" +
                    "          }" +
                    "        }" +
                    "      }" +
                    "    }" +
                    "  }" +
                    "}";

            //convert to string if this doens't work
            JSONObject json = new JSONObject();
            JSONObject variables = new JSONObject();
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
            List<SetData> sets = new ArrayList<>();

            for (int i = 0; i < jsonSets.length(); i++) {
                int setID = jsonSets.getJSONObject(i).getInt("id");
                int bestOf = jsonSets.getJSONObject(i).getInt("totalGames");

                JSONArray slots = jsonSets.getJSONObject(i).getJSONArray("slots");
                Entrant[] players = new Entrant[slots.length()];

                //Store the players in Entrant Objects and exporting that
                for (int j = 0; j < slots.length(); j++) {
                    int newId = slots.getJSONObject(i).getInt("id");
                    players[i] = EventData.getEntrant(newId);
                }

                SetData newSet = new SetData(setID, players, bestOf);

                sets.add(newSet);
            }
            return sets;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }

    }

}
