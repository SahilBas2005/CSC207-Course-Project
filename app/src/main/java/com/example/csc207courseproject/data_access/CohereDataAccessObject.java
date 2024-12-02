package com.example.csc207courseproject.data_access;

import android.util.Log;
import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.use_case.tournament_description.TournamentDescriptionDataAccessInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CohereDataAccessObject implements TournamentDescriptionDataAccessInterface {

    private final String COHERE_API_URL = "https://api.cohere.com/v2/chat"; // Updated URL
    private final String TOKEN = BuildConfig.COHERE; // Your API key
    private JSONObject jsonResponse;
    private CountDownLatch countDownLatch;

    private void sendRequest(String prompt) {
        countDownLatch = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Prepare the request body with messages
        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("model", "command-r-plus-08-2024");  // Updated model name
            JSONArray messages = new JSONArray();

            // Prepare the message from the user
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);

            requestBodyJson.put("messages", messages);  // Added messages array
        } catch (JSONException e) {
            throw new RuntimeException("Failed to create request body", e);
        }

        RequestBody body = RequestBody.create(requestBodyJson.toString(), mediaType);
        Request request = new Request.Builder()
                .url(COHERE_API_URL)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Cohere API Call", "Request failed", e);
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String r = response.body().string();
                    jsonResponse = new JSONObject(r);
                } catch (IOException | JSONException e) {
                    Log.e("Cohere API Response", "Failed to process response", e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

        try {
            countDownLatch.await();  // Wait for the response
            if (jsonResponse != null) {
                Log.d("Cohere API Response", jsonResponse.toString());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Request interrupted", e);
        }
    }

    public String generateTournamentDescription(String eventName, int noOfPlayers) {
        String prompt = "Generate a detailed description for the " + eventName + " tournament, which involves " + noOfPlayers + " players. Include the purpose of the tournament, its format which is Single-Elimination, and any notable features and we will have Losers bracket.";

        sendRequest(prompt);

        try {
            if (jsonResponse != null && jsonResponse.has("message")) {
                JSONObject message = jsonResponse.getJSONObject("message");
                if (message.has("content")) {
                    JSONArray contentArray = message.getJSONArray("content");
                    if (contentArray.length() > 0) {
                        JSONObject contentObject = contentArray.getJSONObject(0);
                        return contentObject.getString("text");  // Extract the 'text' from the content object
                    }
                }
            } else {
                Log.e("Cohere API", "No valid response from API");
                return "Failed to generate tournament description.";
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing Cohere response", e);
        }

        return "Failed to generate tournament description.";
    }

}
