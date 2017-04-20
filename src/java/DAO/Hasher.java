package DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Mark DiFiglio
 * 4/17/2017
 */
public class Hasher {
    
    private static final String saltHash = "zdcW2o0xUz0"; //salt for hashing the password
    
    /**
     * Creates the hash for the password.
     * @param input
     * @return String 40 char string of the hashed password.
     */
    public static String generateHash(String input) {
        input += saltHash;
        StringBuilder hash = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
	} catch (NoSuchAlgorithmException e) {
            // handle error here.
	}
	return hash.toString();
    }
}
