package com.yusurg.backend_yusurg.security;

import com.yusurg.backend_yusurg.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        throw new RuntimeException("Invalid authentication principal");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static Long getCurrentPlayerId() {
        User user = getCurrentUser();
        if (user.getPlayer() == null) {
            throw new RuntimeException("No player linked to this user");
        }
        return user.getPlayer().getId();
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.isAuthenticated() &&
               authentication.getPrincipal() instanceof User;
    }
}
