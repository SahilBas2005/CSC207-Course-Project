package com.example.csc207courseproject.data_access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.csc207courseproject.use_case.login.LoginDataAccessInterface;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UserDataAccessObject implements LoginDataAccessInterface {

    private final String CLIENT_ID = "client_id";
    private final String CLIENT_SECRET = "client_secret";
    private final String REDIRECT_URI = "http://localhost:8080/callback";
    private final String SCOPES = "user.identity%20user.email%20tournament.manager%20tournament.reporter";
    private final String TOKEN_REQUEST_URL = "https://api.start.gg/oauth/access_token";
    private final String API_URL = "https://api.start.gg/gql/alpha";
    private String AUTH_CODE;
    private String ACCESS_TOKEN;

    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public boolean login() {
        // Get authorization code
        try {
            getAuthCode();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return false;
        }
        if (AUTH_CODE == null) {
            return false;
        }

        // Get access token
        try {
            getToken();
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    private boolean getAuthCode() throws URISyntaxException, IOException, InterruptedException {
        // Start a server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Set a request handler
        server.createContext("/callback", new CallbackHandler());

        // Start the server
        server.start();

        String authURL = "https://start.gg/oauth/authorize?response_type=code&client_id=" + System.getenv(CLIENT_ID)
                + "&scope=" + SCOPES
                + "&redirect_uri=" + REDIRECT_URI;

        // Wait at most two minutes for user to log in
        boolean completed = latch.await(2, TimeUnit.MINUTES);
        server.stop(0);
        return completed;
    }

    class CallbackHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Extract authorization code
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            AUTH_CODE = query.split("=")[1];

            // Respond to the browser
            String response = "Authorization code received! You can close this window.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            latch.countDown();
        }
    }

    private void getToken() {
        String jsonBody = "{"
                + "\"grant_type\": \"authorization_code\","
                + "\"code\": \"" + AUTH_CODE + "\","
                + "\"client_id\": \"" + System.getenv(CLIENT_ID) + "\","
                + "\"client_secret\": \"" + System.getenv(CLIENT_SECRET) + "\","
                + "\"redirect_uri\": \"" + REDIRECT_URI + "\","
                + "\"scope\": \"" + SCOPES + "\""
                + "}";

        OkHttpClient client = new OkHttpClient();

        // Create the request body
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));

        // Build the request
        Request request = new Request.Builder()
                .url(TOKEN_REQUEST_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // The response body contains the access token
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                ACCESS_TOKEN = jsonResponse.getString("access_token");
            } else {
                throw new RuntimeException("Request failed. Response Code: " + response.code());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List getTournaments() {
        String q = "query getCurrentUser($page: Int!, $perPage: Int!) { currentUser { name " +
                "tournaments(query: { page: $page, perPage: $perPage }) { nodes { id name } } } }";

        String json = "{ \"query\": \"" + q + "\", \"variables\": { \"page\": 1, \"perPage\": 10 } }";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            final JSONObject jsonResponse = new JSONObject(response.body().string());
            final JSONObject currUser = jsonResponse.getJSONObject("data")
                    .getJSONObject("currentUser");
            System.out.println(currUser.getString("name"));
            return new ArrayList();
                    // currUser.getJSONObject("tournaments").getJSONArray("nodes"));
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }
}