package eca.learnings.userdata.auth;

public class UserContext {
    private static ThreadLocal<String> userAuthToken = new ThreadLocal<>();

    public static void setUserAuthToken(String authToken) {
        userAuthToken.set(authToken);
    }

    public static String getUserAuthToken() {
        return userAuthToken.get();
    }

    public static void clear() {
        userAuthToken.remove();
    }
}

