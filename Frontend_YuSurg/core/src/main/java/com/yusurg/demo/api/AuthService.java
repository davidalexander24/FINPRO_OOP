package com.yusurg.demo.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yusurg.demo.manager.TokenManager;
import com.yusurg.demo.model.AuthResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8082/api/auth";
    private static AuthService instance;
    private Gson gson;

    private AuthService() {
        gson = new Gson();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public AuthResponse sendLoginRequest(String username, String password) throws AuthException {
        try {
            URL url = new URL(BASE_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("username", username);
                requestBody.addProperty("password", password);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println("[AUTH] Login response JSON: " + response.toString());
                        AuthResponse authResponse = gson.fromJson(response.toString(), AuthResponse.class);
                        System.out.println("[AUTH] Parsed userId: " + authResponse.userId);

                        TokenManager.getInstance().saveAuthData(
                            authResponse.token,
                            authResponse.userId,
                            username
                        );

                        authResponse.username = username;

                        return authResponse;
                    }
                } else {
                    String errorMessage = readErrorResponse(conn);
                    throw new AuthException(getErrorMessage(responseCode, errorMessage));
                }
            } finally {
                conn.disconnect();
            }
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("Connection failed: " + e.getMessage());
        }
    }

    public AuthResponse sendSignupRequest(String username, String email, String password) throws AuthException {
        try {
            URL url = new URL(BASE_URL + "/signup");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("username", username);
                requestBody.addProperty("email", email);
                requestBody.addProperty("password", password);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = gson.toJson(requestBody).getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == 200 || responseCode == 201) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        AuthResponse authResponse = gson.fromJson(response.toString(), AuthResponse.class);

                        TokenManager.getInstance().saveAuthData(
                            authResponse.token,
                            authResponse.userId,
                            username
                        );

                        authResponse.username = username;

                        return authResponse;
                    }
                } else {
                    String errorMessage = readErrorResponse(conn);
                    throw new AuthException(getErrorMessage(responseCode, errorMessage));
                }
            } finally {
                conn.disconnect();
            }
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("Connection failed: " + e.getMessage());
        }
    }

    private String readErrorResponse(HttpURLConnection conn) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder errorResponse = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                errorResponse.append(responseLine.trim());
            }

            try {
                JsonObject errorJson = gson.fromJson(errorResponse.toString(), JsonObject.class);
                if (errorJson.has("message")) {
                    return errorJson.get("message").getAsString();
                }
                if (errorJson.has("error")) {
                    return errorJson.get("error").getAsString();
                }
            } catch (Exception ignored) {
            }

            return errorResponse.toString();
        } catch (Exception e) {
            return "Unable to read error response";
        }
    }

    private String getErrorMessage(int responseCode, String serverMessage) {
        switch (responseCode) {
            case 400:
                return serverMessage != null && !serverMessage.isEmpty()
                    ? serverMessage : "Invalid request. Please check your input.";
            case 401:
                return "Invalid username or password.";
            case 403:
                return "Access denied. Please try again.";
            case 404:
                return "User not found.";
            case 409:
                return serverMessage != null && !serverMessage.isEmpty()
                    ? serverMessage : "Username or email already exists.";
            case 500:
                return "Server error. Please try again later.";
            default:
                return serverMessage != null && !serverMessage.isEmpty()
                    ? serverMessage : "An error occurred. Please try again.";
        }
    }

    public void logout() {
        TokenManager.getInstance().clearToken();
    }

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}
