package features.auth;

import core.io.IOManager;
import core.manager.GlobalManager;
import core.models.JSONObject;
import features.auth.instances.Password;

public class UserManager {
    private String username;
    private Password password;
    private String country;
    public boolean isLoggedIn = false;

    public void signup(String username, String password, String country) {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.initUserProfile(username, true);

        // The validation is done inside the Password constructor
        // which means if the password object is created successfully,
        // we can assume the password is valid.
        this.password = new Password(password);
        this.username = username;
        this.country = country;
        isLoggedIn = true;
    }

    public void login(String username, String password) {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.initUserProfile(username, false);

        JSONObject data = ioManager.parseFromFile("profile", password);

        this.password = new Password(password);
        this.username = username;
        this.country = data.getString("country");
        isLoggedIn = true;
    }

    public void reset() {
        username = null;
        password = null;
        country = null;
        isLoggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void writeToFile() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.writeToFile("profile", toJSON());
    }

    @Override
    public String toString() {
        return "AuthContext{username='" + username + "'}";
    }

    private JSONObject toJSON() {
        if (password == null) {
            throw new IllegalStateException("Password is not set");
        }

        JSONObject JSONObject = new JSONObject();
        JSONObject.put("country", country);

        return JSONObject;
    }
}
