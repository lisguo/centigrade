package centigrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
public class ContentIdGenerator {

    private JdbcTemplate template;

    public ContentIdGenerator(JdbcTemplate template){
        this.template = template;
    }
    public Long genId(){
        Long id =  template.query("SELECT MIN(t1.Id+1) FROM content t1 " +
                "WHERE NOT EXISTS(SELECT * FROM movies t2 WHERE t2.Id = t1.Id + 1)" +
                "and NOT EXISTS (Select * from casttocontent where castId = t1.Id+1) " +
                "ORDER BY t1.Id;", new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                while(rs.next()) {
                    return rs.getLong(1);
                }
                return null;
            }
        });

        template.execute("DELETE FROM casttocontent where contentId=" + id + ";");
        return id;
    }

}
