package com.example.csc207courseproject.data_access;

import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import com.example.csc207courseproject.use_case.report_set.ReportSetDataAccessInterface;
import com.example.csc207courseproject.use_case.select_tournament.SelectTournamentDataAccessInterface;
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
        MutateSeedingDataAccessInterface, SelectTournamentDataAccessInterface, ReportSetDataAccessInterface {

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
    public List <Map<Integer, String>> getTournaments() throws JSONException {
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
        List <Map<Integer, String>> userTournaments = new ArrayList();
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
                    Map<Integer, String> userTournament = new HashMap();
                    userTournament.put(
                            tournament.getInt("id"), tournament.getString("name"));
                    userTournaments.add(userTournament);
                    break;
                }
            }
        }

        return userTournaments;
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

    public void setTOKEN(String token) {
        this.TOKEN = token;
    }

    @Override
    public void reportSet(int setID, int winnerID, List<Map<String, Integer>> gameData) {

    }
}
