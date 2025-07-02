package features.auth.data;

import core.io.IOManager;
import core.manager.GlobalManager;
import core.instances.JSONObject;
import features.auth.instances.Password;

public class UserManager {
    private String username;
    private Password password;
    private double footprintGoal = 0.0;
    private String todayChallengeId = "";
    public boolean isLoggedIn = false;

    public void signup(String username, String password) {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.initUserProfile(username, true);

        // The validation is done inside the Password constructor
        // which means if the password object is created successfully, we can assume the password is valid.
        this.password = new Password(password);
        this.username = username;
        isLoggedIn = true;

        GlobalManager.getInstance().initModuleManagers();
    }

    public void login(String username, String password) {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        ioManager.initUserProfile(username, false);

        JSONObject data = ioManager.parseStringFromFile("profile", password);

        this.password = new Password(password);
        this.username = username;
        this.footprintGoal = data.getDouble("footprintGoal");
        String challengeId = data.getString("todayChallengeId");
        this.todayChallengeId = challengeId != null ? challengeId : "";
        isLoggedIn = true;

        GlobalManager.getInstance().initModuleManagers();
    }

    public void reset() {
        username = null;
        password = null;
        todayChallengeId = "";
        isLoggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }


    public double getFootprintGoal() {
        return footprintGoal;
    }

    public String getTodayChallengeId() {
        return todayChallengeId;
    }

    public void setFootprintGoal(double footprintGoal) {
        this.footprintGoal = footprintGoal;
        writeToFile();
    }

    public void setTodayChallengeId(String todayChallengeId) {
        this.todayChallengeId = todayChallengeId;
        writeToFile();
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
        JSONObject.put("footprintGoal", footprintGoal);
        JSONObject.put("todayChallengeId", todayChallengeId);

        return JSONObject;
    }
}
