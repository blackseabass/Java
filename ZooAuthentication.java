/*
 * Eduardo Garcia
 * December 16, 2018
 * zooAuthentication.java
 * Southern New Hamphshire University
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import zooauthentication.Credentials;

public class ZooAuthentication {

    static ArrayList<Credentials> credentialList = new ArrayList<Credentials>();
    private static Scanner input;

	// returns the role of the user
    private static String getRoleByUsername(String username) {
        for (int i = 0; i < credentialList.size(); i++) {
            if (credentialList.get(i).getUserName().equalsIgnoreCase(username)) {
                return credentialList.get(i).getRole();
            }
        }
        return "";
    }

	// loads the credentials
    private static void loadCredentials() throws FileNotFoundException {
        File file = new File("credentials.txt");

        try (Scanner sc = new Scanner(file)) {
            String line;
            String[] credParts;

            while (sc.hasNextLine()) {
                line = sc.nextLine();

                credParts = line.split("\t");
                Credentials credential = new Credentials(credParts[0], credParts[1], credParts[3]);
                credentialList.add(credential);
            }
        }
    }

	// validates the user
    private static boolean validate(String userName, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String md5Hash;
        Credentials currentCredential;

        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        md5Hash = sb.toString();
        for (int i = 0; i < credentialList.size(); i++) {
            currentCredential = credentialList.get(i);
            if (currentCredential.getUserName().equalsIgnoreCase(userName)) {
                return currentCredential.getHashedPassword().equals(md5Hash);
            }
        }

        return false;
    }

	// displays the content
    private static void display(String userName) {
        File file = new File(userName + ".txt");

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            System.out.println("");
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error in reading file: " + userName);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        String userName;
        String password;
        int failedCount = 0;
        boolean isValid;

        input = new Scanner(System.in);
        try {
            loadCredentials();
            while (failedCount < 3) {

                System.out.print("Enter the username: ");
                userName = input.nextLine();

                System.out.print("Enter the password: ");
                password = input.nextLine();

                if (validate(userName, password)) {
                    display(getRoleByUsername(userName));
                    System.out.print("Press return to log out.");
                    System.out.println("");
                    input.nextLine();
                } else {
                    failedCount++;
                    System.out.println("Invalid Credentials. Attempts left: " + (3 - failedCount));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error while reading file");
        }

    }
}
