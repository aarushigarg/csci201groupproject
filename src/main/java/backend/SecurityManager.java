package backend;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityManager {

    public static String hashPassword(String password) {
        try {
            // Get an instance of MessageDigest with SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convert the password to bytes, compute the hash
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert the hash to a Base64 string
            return Base64.getEncoder().encodeToString(hashedBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found.", e);
        }
    }

    public boolean verifyPassword(String hashedPassword, String inputPassword) {
        // Hash the input password
        String hashedInput = hashPassword(inputPassword);

        // Compare the hashes
        return hashedPassword.equals(hashedInput);
    }
}