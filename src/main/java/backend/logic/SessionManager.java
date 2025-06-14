package backend.logic;

import backend.model.User;

/**
 * Manages the current user session in the application.
 * Provides static methods to set, retrieve, and clear the logged-in user.
 */

public class SessionManager {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}
