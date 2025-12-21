package com.yusurg.demo.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yusurg.demo.manager.TokenManager;
import com.yusurg.demo.model.DifficultyLevel;
import com.yusurg.demo.model.PlayerData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BackendService {
    private static final String BASE_URL = "http://localhost:8082/api";
    private static BackendService instance;
    private Gson gson;

    private BackendService() {
        gson = new Gson();
    }

    public static BackendService getInstance() {
        if (instance == null) {
            instance = new BackendService();
        }
        return instance;
    }

    private void addAuthHeader(HttpURLConnection conn) {
        String authHeader = TokenManager.getInstance().getAuthorizationHeader();
        if (authHeader != null) {
            conn.setRequestProperty("Authorization", authHeader);
        }
    }

    private void checkUnauthorized(int responseCode) throws UnauthorizedException {
        if (responseCode == 401) {
            TokenManager.getInstance().clearToken();
            throw new UnauthorizedException("Session expired. Please login again.");
        }
    }

    @Deprecated
    public PlayerData loginOrRegister(String username) throws Exception {
        URL url = new URL(BASE_URL + "/players/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            addAuthHeader(conn);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("username", username);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            checkUnauthorized(responseCode);

            if (responseCode == 200 || responseCode == 201) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return gson.fromJson(response.toString(), PlayerData.class);
                }
            } else {
                throw new Exception("Server returned error code: " + responseCode);
            }
        } finally {
            conn.disconnect();
        }
    }

    public Long startGameSession(long playerId, DifficultyLevel difficulty) throws Exception {
        URL url = new URL(BASE_URL + "/games/start");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            addAuthHeader(conn);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("playerId", playerId);
            requestBody.addProperty("difficulty", difficulty.name());

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            checkUnauthorized(responseCode);

            if (responseCode == 200 || responseCode == 201) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JsonObject responseObj = gson.fromJson(response.toString(), JsonObject.class);
                    return responseObj.get("id").getAsLong();
                }
            } else {
                String errorMessage = "";
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    errorMessage = errorResponse.toString();
                } catch (Exception e) {
                    errorMessage = "Unable to read error response";
                }
                System.err.println("Start game session error response: " + errorMessage);
                throw new Exception("Failed to start game session: " + responseCode);
            }
        } finally {
            conn.disconnect();
        }
    }

    public PlayerData endGameSession(long sessionId, String outcome, int score, float duration, long playerId) throws Exception {
        URL url = new URL(BASE_URL + "/games/end");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            addAuthHeader(conn);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("sessionId", sessionId);
            requestBody.addProperty("outcome", outcome);
            requestBody.addProperty("score", score);
            requestBody.addProperty("gameDurationSeconds", duration);

            System.out.println("[DEBUG] End game session request: " + gson.toJson(requestBody));

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("[DEBUG] End game session response code: " + responseCode);
            checkUnauthorized(responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("[DEBUG] End game session response: " + response.toString());
                }
                return getPlayerData(playerId);
            } else {
                String errorMsg = "";
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    errorMsg = errorResponse.toString();
                } catch (Exception ignored) {}
                System.err.println("[DEBUG] End game session error: " + errorMsg);
                throw new Exception("Failed to end game session: " + responseCode + " - " + errorMsg);
            }
        } finally {
            conn.disconnect();
        }
    }

    public PlayerData getPlayerData(long playerId) throws Exception {
        URL url = new URL(BASE_URL + "/players/" + playerId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            addAuthHeader(conn);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            System.out.println("[DEBUG] Get player data response code: " + responseCode);
            checkUnauthorized(responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("[DEBUG] Get player data response: " + response.toString());
                    PlayerData player = gson.fromJson(response.toString(), PlayerData.class);
                    System.out.println("[DEBUG] Parsed player - XP: " + player.totalExp + ", Level: " + player.surgeonLevel);
                    return player;
                }
            } else {
                throw new Exception("Failed to get player data: " + responseCode);
            }
        } finally {
            conn.disconnect();
        }
    }

    public PlayerData addPlayerXP(long playerId, int xpGained) throws Exception {
        URL url = new URL(BASE_URL + "/players/" + playerId + "/addxp?xp=" + xpGained);
        System.out.println("[DEBUG] Add XP URL: " + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            addAuthHeader(conn);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(new byte[0]);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("[DEBUG] Add XP response code: " + responseCode);
            checkUnauthorized(responseCode);

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("[DEBUG] Add XP response: " + response.toString());
                    PlayerData player = gson.fromJson(response.toString(), PlayerData.class);
                    System.out.println("[DEBUG] After XP add - Total XP: " + player.totalExp + ", Level: " + player.surgeonLevel);
                    return player;
                }
            } else {
                String errorMsg = "";
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    errorMsg = errorResponse.toString();
                } catch (Exception ignored) {}
                System.err.println("[DEBUG] Add XP error: " + errorMsg);
                throw new Exception("Failed to add player XP: " + responseCode + " - " + errorMsg);
            }
        } finally {
            conn.disconnect();
        }
    }

    public static class UnauthorizedException extends Exception {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
