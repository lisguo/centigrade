package centigrade.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    // Constants for hashing password
    private static final int ITERATIONS = 5000;
    private static final int KEY_LENGTH = 256;

    public boolean isValidRegister(String email){
        Account a = getAccountByEmail(email);
        return a == null;
    }

    public boolean isValidLogin(String email){
        Account a = getAccountByEmail(email);
        return a != null;
    }

    public byte[] hashPassword(String password, byte[] salt){
        try {
            char[] passwordArr = password.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(passwordArr, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAccount(String email, String password, String firstName, String lastName){
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hashedPassword = hashPassword(password, salt);

        Account a = new Account();
        a.setEmail(email);
        a.setPassword(hashedPassword);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setAccountType(AccountType.USER);
        a.setIsActive(0);
        a.setSalt(salt);
        accountRepository.save(a);
    }

    public Account getAccountById(long id){
        return accountRepository.findAccountById(id);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findAccountByEmail(email);
    }
}
