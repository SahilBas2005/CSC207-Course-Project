package data_access;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class APIDataAccessObject {

    private static final String TOKEN = "token";
    private static final String API_URL = "https://api.start.gg/gql/alpha";

    public String testKey(){
        return System.getenv(TOKEN);
    }
    public HashMap<Integer, String> getEvent() {
        String q = "query getEventId($slug: String) {event(slug: $slug) {id name}}";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"slug\": \"tournament/ultimate-tmu-ep-4/event/ultimate-singles\"}}";
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
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            String jsonResponse = response.body().string();
            System.out.println(jsonResponse);

            // Extract the event ID and name
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject eventData = jsonObject.getJSONObject("data").getJSONObject("event");
            Integer eventId = eventData.getInt("id");
            String eventName = eventData.getString("name");
            HashMap<Integer, String> event = new HashMap<>();
            event.put(eventId, eventName);
            return event;
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }


    }

    public void getPhaseSeeding() {
        String q = "query GetPhaseSeeds($phaseId: ID!) { phase(id: $phaseId) { id numSeeds seeds(query: { page: 1, perPage: 60 }) { nodes { id seedNum entrant { id participants { id gamerTag } } } } } }";
        String v = "{ \"phaseId\": 519453 }";

        String json = "{ \"query\": \"" + q + "\", \"variables\": " + v + "}";
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
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            String jsonResponse = response.body().string();
            System.out.println(jsonResponse);
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }


    }

    public HashMap<String, JSONArray> getPhases() {
        String q = "query EventPhases($eventId: ID!) { event(id: $eventId) { name phases { name seeds(query: " +
                "{ page: 1, perPage: 10 }) { nodes { id seedNum entrant { id participants { id gamerTag } } } } } } }";

        String v = "{\"eventId\": 78790}";

        String json = "{ \"query\": \"" + q + "\", \"variables\": " + v + "}";
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
            if(!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            String jsonResponse = response.body().string();
//            System.out.println(jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray phases = jsonObject.getJSONObject("data").getJSONObject("event")
                    .getJSONArray("phases");

            HashMap<String, JSONArray> phasesMap = new HashMap<>();
            for (int i = 0; i < phases.length(); i++) {
                JSONObject phase = phases.getJSONObject(i);
                String name = phase.getString("name");
                JSONArray seeds = phase.getJSONObject("seeds").getJSONArray("nodes");
                phasesMap.put(name, seeds);
            }
            return phasesMap;
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }
}
