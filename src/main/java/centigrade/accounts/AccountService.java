package centigrade.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSender mailSender;

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

    public Account addAccount(String email, String password, String firstName, String lastName){
        // Encrypt password
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hashedPassword = hashPassword(password, salt);

        String nonce = UUID.randomUUID().toString();

        Account a = new Account();
        a.setEmail(email);
        a.setPassword(hashedPassword);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setAccountType(AccountType.USER);
        a.setIsActive(0);
        a.setSalt(salt);
        a.setNonce(nonce);
        accountRepository.save(a);

        return a;
    }
    public void updateAccount(Account a ,String email, String password, String firstName, String lastName){
        // Generate a random salt
//        SecureRandom random = new SecureRandom();
        byte[] salt = a.getSalt();

        byte[] hashedPassword=null;
        if(password!=null)hashedPassword= hashPassword(password, salt);

//        Account a = new Account();
        if(email!=null) a.setEmail(email);
        if(hashedPassword!=null)a.setPassword(hashedPassword);
        if(firstName!=null) a.setFirstName(firstName);
        if(lastName!=null)a.setLastName(lastName);
//        a.setAccountType(AccountType.USER);
//        a.setIsActive(0);
//        a.setSalt(salt);
        accountRepository.save(a);
    }

    public void sendVerificationEmail(Account a, String link, String subject){
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setTo(a.getEmail());
            helper.setSubject(subject);
            helper.setText(
                    "Hello " + a.getFirstName() + ", \n\n"
                    + "Click this link to verify you account: "
                    + link + a.getNonce()
            );
            this.mailSender.send(message);
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }

    public Account verifyAccount(String nonce){
        Account a = accountRepository.findAccountByNonce(nonce);
        if(a != null){
            a.setIsActive(1);
            accountRepository.save(a);
        }
        return a;
    }

    public Account getAccountById(long id){
        return accountRepository.findAccountById(id);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findAccountByEmail(email);
    }
}
