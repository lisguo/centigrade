package centigrade.accounts;

import centigrade.TVShows.TVShow;
import centigrade.TVShows.TVShowService;
import centigrade.movies.Movie;
import centigrade.movies.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    JdbcTemplate template;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TVShowService tvShowService;

    // Constants for hashing password
    private static final int ITERATIONS = 5000;
    private static final int KEY_LENGTH = 256;

    public List<WishListItem> getWishList(Account a){
        return template.query("SELECT isMovie, contentId FROM wishlistitems WHERE accountId='" + a.getId() + "'", new ResultSetExtractor<List<WishListItem>>() {
            @Override
            public List<WishListItem> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<WishListItem> items = new ArrayList<WishListItem>();
                Movie m;
                TVShow t;

                while (rs.next()) {
                    if(rs.getBoolean(1)){
                        m = movieService.getMovieById(rs.getLong(2));
                        if(m != null){
                            items.add(m);
                        }
                    }else{
                        t = tvShowService.getTVShowById(rs.getLong(2));
                        if(t != null){
                            items.add(t);
                        }
                    }
                }
                return items;
            }
        });
    }

    public List<WishListItem> getNotInterestedList(Account a){
        return template.query("SELECT isMovie, contentId FROM notinterestedlistitems WHERE accountId='" + a.getId() + "'", new ResultSetExtractor<List<WishListItem>>() {
            @Override
            public List<WishListItem> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<WishListItem> items = new ArrayList<WishListItem>();
                Movie m;
                TVShow t;

                while (rs.next()) {
                    if(rs.getBoolean(1)){
                        m = movieService.getMovieById(rs.getLong(2));
                        if(m != null){
                            items.add(m);
                        }
                    }else{
                        t = tvShowService.getTVShowById(rs.getLong(2));
                        if(t != null){
                            items.add(t);
                        }
                    }
                }
                return items;
            }
        });
    }

    public void removeFromWishList(long accountId, long contentID){
        String query = "delete from wishlistitems where accountId = ? and contentId = ?";
        Object[] args = new Object[] {accountId, contentID};

        template.update(query, args);
    }

    public void removeFromNotInterestedList(long accountId, long contentID){
        String query = "delete from notinterestedlistitems where accountId = ? and contentId = ?";
        Object[] args = new Object[] {accountId, contentID};

        template.update(query, args);
    }

    public Boolean insertIntoWishList(long accountID, long contentID, boolean isMovie){
        String query = "insert into wishlistitems(accountId, isMovie, contentId) values(?,?,?)";
        return template.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setLong(1, accountID);
                ps.setBoolean(2, isMovie);
                ps.setLong(3, contentID);

                return ps.execute();
            }
        });
    }

    public Boolean insertIntoNotInterestedList(long accountID, long contentID, boolean isMovie){
        String query = "insert into notinterestedlistitems(accountId, isMovie, contentId) values(?,?,?)";
        return template.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setLong(1, accountID);
                ps.setBoolean(2, isMovie);
                ps.setLong(3, contentID);

                return ps.execute();
            }
        });
    }

    public boolean isValidRegister(String email) {
        Account a = getAccountByEmail(email);
        return a == null;
    }

    public boolean isValidLogin(String email) {
        Account a = getAccountByEmail(email);
        return a != null;
    }

    public byte[] hashPassword(String password, byte[] salt) {
        try {
            char[] passwordArr = password.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(passwordArr, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(env.getProperty("encryption_algorithm"));
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

    public Account addAccount(String email, String password, String firstName, String lastName) {
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

    public void updateAccount(Account a, String email, String password, String firstName, String lastName) {
        byte[] salt = a.getSalt();

        byte[] hashedPassword = null;
        if (password != null){
            hashedPassword = hashPassword(password, salt);
        }

        if (email != null){
            a.setEmail(email);
        }
        if (hashedPassword != null){
            a.setPassword(hashedPassword);
        }
        if (firstName != null){
            a.setFirstName(firstName);
        }
        if (lastName != null){
            a.setLastName(lastName);
        }

        accountRepository.save(a);
    }

    public void sendVerificationEmail(Account a, String link, String subject) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(a.getEmail());
            helper.setSubject(subject);
            helper.setText(
                    "Hello " + a.getFirstName() + ", \n\n"
                            + "Click this link to verify your account: "
                            + link + a.getNonce()
            );
            this.mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Account verifyAccount(String nonce) {
        Account a = accountRepository.findAccountByNonce(nonce);
        if (a != null) {
            a.setIsActive(1);
            accountRepository.save(a);
        }
        return a;
    }

    public Account getAccountById(long id) {
        return accountRepository.findAccountById(id);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    public String getUserPhotoURL() {
        String path = env.getProperty("user_photo_dir");
        return path;
    }

}
