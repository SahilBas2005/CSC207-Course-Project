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
import use_case.select_phase.SelectPhaseDataAccessInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIDataAccessObject implements SelectPhaseDataAccessInterface, MainDataAccessInterface {

    private static final String TOKEN = "token";
    private static final String API_URL = "https://api.start.gg/gql/alpha";

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

    @Override
    public Entrant[] getEntrantsInEvent(int eventID) {
        String q = "hi";/*"""
          query EventEntrants($eventId: ID!, $page: Int!, $perPage: Int!) {
          event(id: $eventId) {
            id
            name
            entrants(query: {
              page: $page
              perPage: $perPage
            }) {
              pageInfo {
                total
                totalPages
              }
              nodes {
                id
                participants {
                  id
                  gamerTag
                }
              }
            }
          }
        }
                        """*/;
        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"eventID\": \"" + eventID + "\", \"page\": 1, \"perPage\": 64}}";

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
            String jsonResponse = response.body().string();
            System.out.println(jsonResponse);
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
        return new Entrant[0];
    }

    @Override
    public Map<String, Integer> getPhaseIDs(int eventID) {
        return new HashMap<String, Integer>();
    }

    @Override
    public List<Integer> getSeedinginPhase(int phaseID) {
        return new ArrayList<>();
    }
}
