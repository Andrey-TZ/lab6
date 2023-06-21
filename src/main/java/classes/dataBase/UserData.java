package classes.dataBase;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData implements Serializable {
    private final String login;
    private final String password;
    private boolean isRegistrated = false;

    public UserData(String login, String password){
        this.login= login;
        this.password = password;
    }
    public UserData(String login, String password, boolean flag){
        this.login= login;
        this.password = password;
        isRegistrated = flag;
    }
    public String getLogin(){
        return login;
    }

    public boolean isRegistrated(){
        return isRegistrated;
    }
    public void setRegistrated(boolean flag){
        isRegistrated = flag;
    }

    public String getHashedPassword() {
        return hashPassword(password);
    }
    private static String hashPassword(String password)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] salt = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, salt);
            String hashPassword = no.toString(16);
            while (hashPassword.length() < 32) {
                hashPassword = "0" + hashPassword;
            }
            System.out.println(hashPassword);
            return hashPassword;
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserData other = (UserData) object;
        return login.equals(other.getLogin()) && password.equals(other.password) && hashPassword(password).equals(hashPassword(other.password));
    }
}
