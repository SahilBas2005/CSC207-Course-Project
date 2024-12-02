package com.example.csc207courseproject.data_access.OAuth;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import com.example.csc207courseproject.BuildConfig;
import com.example.csc207courseproject.use_case.login.LoginOAuthDataAccessInterface;
import fi.iki.elonen.NanoHTTPD;
import okhttp3.*;

/**
 * A class responsible for logging the user into their start.gg account and getting the user's access token.
 */
public class OAuthDataAccessObject implements LoginOAuthDataAccessInterface {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    private static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final String SCOPES = "user.identity%20user.email%20tournament.manager%20tournament.reporter";
    private static final String TOKEN_REQUEST_URL = "https://api.start.gg/oauth/access_token";
    private String authCode;
    private String accessToken;
    private OAuthServer oAuthServer;
    private Thread serverThread;
    private CountDownLatch latch;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Starts a local server and returns the URL for logging into start.gg.
     * @return the log in URL for start.gg
     */
    public String getAuthUrl() {
        serverThread = new Thread(() -> {
            try {
                oAuthServer = new OAuthServer();
            }
            catch (IOException e) {
                throw new OAuthException("The server failed to start.");
            }
        });
        serverThread.start();

        return "https://start.gg/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + CLIENT_ID
                + "&scope=" + SCOPES
                + "&redirect_uri=" + REDIRECT_URI;
    }

    /**
     * Gets the user's token for making API calls to start.gg.
     * @return the access token
     */
    public String getToken() {
        latch = new CountDownLatch(1);
        final String jsonBody = "{"
                + "\"grant_type\": \"authorization_code\","
                + "\"code\": \"" + authCode + "\","
                + "\"client_id\": \"" + CLIENT_ID + "\","
                + "\"client_secret\": \"" + CLIENT_SECRET + "\","
                + "\"redirect_uri\": \"" + REDIRECT_URI + "\","
                + "\"scope\": \"" + SCOPES + "\""
                + "}";

        final OkHttpClient client = new OkHttpClient();

        // Create the request body
        final RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));

        // Build the request
        final Request request = new Request.Builder()
                .url(TOKEN_REQUEST_URL)
                .post(body)
                .build();

        // Cannot run network operation on main thread.
        // Run the network call on another thread and execute the callback function once the call is complete.
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException evt) {
                latch.countDown();
                throw new OAuthException(evt.getMessage());
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(responseBody);
                    }
                    catch (JSONException evt) {
                        latch.countDown();
                        throw new OAuthException(evt.getMessage());
                    }
                    try {
                        accessToken = jsonResponse.getString("access_token");
                        latch.countDown();
                    }
                    catch (JSONException evt) {
                        latch.countDown();
                        throw new OAuthException(evt.getMessage());
                    }
                }
                else {
                    latch.countDown();
                    throw new OAuthException(response.message());
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new OAuthException("Server thread interrupted");

        }
        return accessToken;
    }

    /**
     * Stops the local server.
     */
    public void stopServer() {
        oAuthServer.stop();
        serverThread.interrupt();
    }

    /**
     * Fires a property changed event when the auth code is received by the server.
     */
    public void authCodeReceived() {
        // Looper.getMainLooper() is used to get the main thread
        // Handler is used to schedule a task on a specific thread
        final Handler mainHandler = new Handler(Looper.getMainLooper());

        // Fire the property change event on the main thread
        mainHandler.post(() -> {
            this.support.firePropertyChange("auth_code_received", null, authCode);
        }
        );
    }

    /**
     * Adds a listener to listen in on when the auth code is received.
     * @param listener The PropertyChangeListener to be added
     */
    public void addListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    /**
     * A class for creating and handling requests on a local server.
     */
    class OAuthServer extends NanoHTTPD {

        static final int PORT = 8080;

        /**
         * Starts a localhost server at port 8080.
         * @throws IOException if issues are encountered when starting the server
         */
        public OAuthServer() throws IOException {
            // set port to 8080
            super(PORT);
            // start server
            start();
        }

        /**
         * Handles incoming HTTP requests.
         * @param session contains information about the current request
         */
        @Override
        public Response serve(IHTTPSession session) {
            final Map<String, List<String>> params = session.getParameters();
            if (params.containsKey("code")) {
                authCode = params.get("code").get(0);
                authCodeReceived();

                // message to display on the server
                return newFixedLengthResponse("Authorization code received! You may close this window.");
            }
            return null;
        }
    }
}
