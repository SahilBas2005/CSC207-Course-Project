package data_access;

import entities.Entrant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.main.MainDataAccessInterface;
import use_case.mutate_seeding.MutateSeedingDataAccessInterface;
import use_case.report_set.ReportSetDataAccessInterface;
import use_case.select_phase.SelectPhaseDataAccessInterface;

import java.io.IOException;
import java.util.*;

public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MainDataAccessInterface,
        MutateSeedingDataAccessInterface, ReportSetDataAccessInterface {

    private final String TOKEN = "token";
    private final String API_URL = "https://api.start.gg/gql/alpha";
    private Map<Integer, Integer> idToSeedID = new HashMap<>();
    private int initialPhaseID;
    private List<Integer> overallSeeding;

    /**
     * Gets the event id of a given event.
     * @param eventLink The link of the event
     * @return The id of the event
     */
    public int getEventId(String eventLink) {
        String q = "query getEventId($slug: String) {event(slug: $slug) {id name}}";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"slug\": \"" + eventLink + "\"}}";
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONObject("data").getJSONObject("event").getInt("id");
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
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

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Create request for entrants
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            // Send request
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
            final JSONArray jsonEntrants = jsonResponse.getJSONObject("data").getJSONObject("event")
                    .getJSONObject("entrants").getJSONArray("nodes");

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
                entrants[i] = new Entrant(names, sponsors, id, userIDs);
            }
            return entrants;
        }
        catch (IOException | JSONException event) {
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

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Create request for phases
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            // Send request
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
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
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    private void createOverallSeeding(){
        // Create query
        String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)" +
                "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}" +
                "nodes {seedNum id entrant {id}}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + initialPhaseID + "\", \"page\": 1, \"perPage\": 64}}";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Create request for seeds
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            // Send request
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
            final JSONArray jsonSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes");

            // Create list of seeds and fill it in seeded order
            List<Integer> seeding = new ArrayList<>();

            for (int i = 0; i < jsonSeeds.length(); i++) {
                int id = jsonSeeds.getJSONObject(i).getJSONObject("entrant").getInt("id");
                idToSeedID.put(id, jsonSeeds.getJSONObject(i).getInt("id"));
                seeding.add(id);
            }
            overallSeeding = seeding;
        }
        catch (IOException | JSONException event) {
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
        createOverallSeeding();
        // Create query for the number of entrants in the phase
        String q = "query PhaseSeeds($phaseId: ID!, $page: Int!, $perPage: Int!) {phase(id:$phaseId)" +
                "{seeds(query: {page: $page perPage: $perPage}){pageInfo {total totalPages}" +
                "nodes {seedNum}}}}";
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"phaseId\": \"" + phaseID + "\", \"page\": 1, \"perPage\": 64}}";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Create request for seeds
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            // Send request
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
            final int numSeeds = jsonResponse.getJSONObject("data").getJSONObject("phase")
                    .getJSONObject("seeds").getJSONArray("nodes").length();

            // Return seeding sliced to include only this phase
            return overallSeeding.subList(0, numSeeds);
        }
        catch (IOException | JSONException event) {
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

        // Generate seed mapping
        List<Map<String, Integer>> seedMapping = new ArrayList<>();
        for(int i = 0; i < overallSeeding.size(); i++) {
            Map<String, Integer> seedMap = new TreeMap<>();
            seedMap.put("seedId", idToSeedID.get(overallSeeding.get(i)));
            seedMap.put("seedNum", i + 1);
            seedMapping.add(seedMap);
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

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Create request for seeds
        RequestBody body = RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + System.getenv(TOKEN))
                .post(body)
                .build();
        try {
            // Send request
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public void reportSet(int setID, int winnerID, List<Map<String, Integer>> gameData) {

    }
}
