package crypto.users.db;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class HASHCode
{
    public static String hash(String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes     = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        }
        catch(NoSuchAlgorithmException e){throw new RuntimeException(e);}
    }
}
