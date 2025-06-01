package core.utils;

import features.auth.AuthContext;

public class ContextManager {
    private static ContextManager instance;

    private AuthContext authContext;

    private ContextManager() {
        authContext = new AuthContext();
    }

    public static ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }

        return instance;
    }

    public AuthContext getAuthContext() {
        return authContext;
    }

    public void setAuthContext(AuthContext authContext) {
        this.authContext = authContext;
    }
}
