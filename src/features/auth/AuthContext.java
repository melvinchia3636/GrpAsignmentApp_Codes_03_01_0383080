package features.auth;

public class AuthContext {
    private String username;
    private char[] password;
    private String country;

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "AuthContext{username='" + username + "'}";
    }
}
