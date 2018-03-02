import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Users {

    private static Map<Integer, Long> usersDB;

    private static Users users;

    private Users() {
        usersDB = new HashMap<Integer, Long>();
    }

    public static Users getUsers() {
        if(users == null){
            return new Users();
        }
        return users;
    }

    public boolean isRegisteredUser(int credentials){
        boolean isRegistered = usersDB.containsValue(credentials);
        Long id = usersDB.get(credentials);
        if(id != null) isRegistered = true;
        return isRegistered;
    }

    public boolean isSameChat(int credentials, long chat_id){
        return usersDB.get(credentials) == chat_id;
    }

    public int addUser(int credentials, long chat_id) throws InvalidKeySpecException, NoSuchAlgorithmException {
        usersDB.put(credentials, chat_id);
        return getCode(credentials);
    }

    public int getCode(int intCredentials) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String credentials = String.valueOf(intCredentials);
        TimeBasedOneTimePasswordGenerator totp =
                new TimeBasedOneTimePasswordGenerator(5, TimeUnit.MINUTES);

        byte[] salt = {
                (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
        };

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(credentials.toCharArray(), salt, 20, 512);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), totp.getAlgorithm());


        int code = 0;
        try {
            code = totp.generateOneTimePassword(secretKey, new Date());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return code;
    }
}
