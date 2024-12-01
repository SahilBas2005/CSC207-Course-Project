package com.example.csc207courseproject.data_access;

import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.entities.Entrant;
import com.example.csc207courseproject.entities.Participant;
import com.example.csc207courseproject.use_case.finance.FinanceDataAccessInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.csc207courseproject.use_case.main.MainDataAccessInterface;
import com.example.csc207courseproject.use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import com.example.csc207courseproject.use_case.select_phase.SelectPhaseDataAccessInterface;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MainDataAccessInterface,
        MutateSeedingDataAccessInterface, FinanceDataAccessInterface {

    private final String TOKEN = BuildConfig.token;
    private final String API_URL = "https://api.start.gg/gql/alpha";
    private Map<Integer, Integer> idToSeedID = new HashMap<>();
    private int initialPhaseID;
    private List<Integer> overallSeeding;
    private JSONObject jsonResponse;
    private CountDownLatch countDownLatch;
    private static HashMap<Integer, Participant> participantPaymentStatus = new HashMap<>();

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
     * @return A list of all entrants as entrant objects
     */
    @Override
    public Entrant[] getEntrantsInEvent(int eventID) {
        // Create query
        String q = "query EventEntrants($eventId: ID!, $page: Int!, $perPage: Int!) {event(id: $eventId)" +
                "{entrants(query: {page: $page perPage: $perPage}) { pageInfo{total totalPages}" +
              "nodes {id participants {id prefix gamerTag}}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"eventId\": \"" + eventID + "\", \"page\": 1, \"perPage\": 64}}";

        sendRequest(json);
        try {
            final JSONArray jsonEntrants = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("entrants").getJSONArray("nodes");
            jsonResponse = null;

            // Create entrants array and fill it in
            Entrant[] entrants = new Entrant[jsonEntrants.length()];

            for (int i = 0; i < jsonEntrants.length(); i++) {
                JSONObject entrantObject = jsonEntrants.getJSONObject(i);
                JSONArray jsonParticipants = entrantObject.getJSONArray("participants");
                int id = entrantObject.getInt("id");

                // Fill in the names and user ids for each player on a team
                int[] userIDs = new int[jsonParticipants.length()];
                String[] names = new String[jsonParticipants.length()];
                String[] sponsors = new String[jsonParticipants.length()];
                for (int j = 0; j < jsonParticipants.length(); j++) {
                    userIDs[j] = jsonParticipants.getJSONObject(j).getInt("id");
                    names[j] = jsonParticipants.getJSONObject(j).getString("gamerTag");

                    // Ensure sponsor isn't null
                    if (jsonParticipants.getJSONObject(j).get("prefix").equals(null)){
                        sponsors[j] = "";
                    } else {
                        sponsors[j] = jsonParticipants.getJSONObject(j).getString("prefix");
                    }
                }
                entrants[i] = new Entrant(names, sponsors, id, userIDs, false);
            }
            return entrants;
        }
        catch (JSONException event) {
            throw new RuntimeException(event);
        }
    }

    public HashMap<Integer, Participant> fetchParticipantPaymentStatus(int eventID) {
        // Create query
        String gotSlug = getTournamentSlug(String.valueOf(eventID), TOKEN) ;

        String q = "query GetTournamentBySlug($slug: String!, $participantQuery: ParticipantPaginationQuery!) {" +
                "tournament(slug: $slug) {" +
                "id name participants(query: $participantQuery, isAdmin: true) {" +
                "pageInfo {total perPage page} " +
                "nodes {id gamerTag email checkedIn verified}}}}";

        String json = "{ \"query\": \"" + q + "\", " +
                "\"variables\": { " +
                "\"slug\": \"" + gotSlug + "\", " +
                "\"participantQuery\": { " +
                "\"page\": 1, " +
                "\"perPage\": 500, " +
                "\"filter\": { \"unpaid\": true } " +
                "} " +
                "} }";

        sendRequest(json); // Assuming sendRequest populates jsonResponse

        try {
            final JSONArray jsonParticipants = jsonResponse.getJSONObject("data")
                    .getJSONObject("tournament")
                    .getJSONObject("participants")
                    .getJSONArray("nodes");

            for (int i = 0; i < jsonParticipants.length(); i++) {
                JSONObject participantObject = jsonParticipants.getJSONObject(i);

                int participantId = participantObject.getInt("id");
                String gamerTag = participantObject.getString("gamerTag");
                String name = gamerTag;  // Assuming gamerTag is used as the name (adjust if needed)
                String sponsor = "";  // If sponsor information is available, replace with it (or leave empty for now)
                boolean isUnpaid = !participantObject.getBoolean("verified"); // Assuming "verified" means "paid"

                // Create Participant object with the data
                Participant participant = new Participant(participantId, 0, name, sponsor); // User ID is assumed to be 0 for now
                if (isUnpaid) {
                    // Optionally mark as unpaid, if needed (e.g., mark the participant as paid if verified)
                    participant.markAsPaid();  // This will mark the participant as paid if verified
                }

                // Add Participant object to the HashMap with participant ID as key
                participantPaymentStatus.put(participantId, participant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse participant JSON response", e);
        } catch (Exception e) {
            // generic exception
            e.printStackTrace();

        }

        return participantPaymentStatus;
    }

    @Override
    public void modifyParticipantPaymentStatus(int participantId) {
        Participant participantOfInterest = participantPaymentStatus.get(participantId);

        // ensure that the participant of interest is not Null
        assert participantOfInterest != null;
        participantOfInterest.markAsPaid();
    }

    @Override
    public Map<Integer, Participant> getParticipantPaymentStatus() {
        return participantPaymentStatus;
    }

    public String getTournamentSlug(String eventID, String apiKey) {
        String query = "{ \"query\": \"query GetTournamentByEventID($eventID: ID!) { event(id: $eventID) { tournament { id slug name } } }\", " +
                "\"variables\": { \"eventID\": \"" + eventID + "\" } }";

        sendRequest(query);

        // Parse the JSON response
        try {
            final JSONObject jsonTournamentObject = jsonResponse.getJSONObject("data")
                    .getJSONObject("event")
                    .getJSONObject("tournament");
            return jsonTournamentObject.getString("slug");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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


}
