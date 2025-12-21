package com.yusurg.demo.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * TokenManager - Manages JWT token storage and retrieval
 * Uses LibGDX Preferences for persistent storage
 */
public class TokenManager {
    private static final String PREFS_NAME = "YuSurgPrefs";
    private static final String TOKEN_KEY = "jwt_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String USERNAME_KEY = "username";

    private static TokenManager instance;
    private Preferences preferences;

    private TokenManager() {
        preferences = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    /**
     * Save authentication data after successful login/signup
     */
    public void saveAuthData(String token, long userId, String username) {
        preferences.putString(TOKEN_KEY, token);
        preferences.putLong(USER_ID_KEY, userId);
        preferences.putString(USERNAME_KEY, username);
        preferences.flush();
        System.out.println("Auth data saved for user: " + username);
    }

    /**
     * Save JWT token
     */
    public void saveToken(String token) {
        preferences.putString(TOKEN_KEY, token);
        preferences.flush();
    }

    /**
     * Get stored JWT token
     */
    public String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    /**
     * Get stored user ID
     */
    public long getUserId() {
        return preferences.getLong(USER_ID_KEY, -1);
    }

    /**
     * Get stored username
     */
    public String getUsername() {
        return preferences.getString(USERNAME_KEY, null);
    }

    /**
     * Clear all authentication data (logout)
     */
    public void clearToken() {
        preferences.remove(TOKEN_KEY);
        preferences.remove(USER_ID_KEY);
        preferences.remove(USERNAME_KEY);
        preferences.flush();
        System.out.println("Auth data cleared");
    }

    /**
     * Check if user is logged in (has valid token)
     */
    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    /**
     * Get Authorization header value
     */
    public String getAuthorizationHeader() {
        String token = getToken();
        if (token != null && !token.isEmpty()) {
            return "Bearer " + token;
        }
        return null;
    }
}

