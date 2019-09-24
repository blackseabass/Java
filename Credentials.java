/*
 * Eduardo Garcia
 * December 16, 2018
 * Credentials.java
 * Southern New Hamphshire University
 */
package zooauthentication;

public class Credentials {

    String userName;
    String hashedPassword;
    String role;

    public Credentials(String userName, String hashedPassword, String role) {
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRole() {
        return role;
    }
}
