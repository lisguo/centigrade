package centigrade.critic_applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class CriticApplicationService {
    @Autowired
    CriticApplicationRepository applicationRepository;

    public CriticApplication submitCriticApplication(long accountId, String sourceName, String sourceUrl, String resumeText){
        CriticApplication application = new CriticApplication();
        application.setAccount(accountId);
        application.setSourceName(sourceName);
        application.setSourceUrl(sourceUrl);
        application.setResumeText(resumeText);

        applicationRepository.save(application);
        return application;
    }

    public boolean exists(long accountId){
        CriticApplication application = applicationRepository.findCriticApplicationByAccount(accountId);
        if(application != null){
            return true;
        }
        return false;
    }
}
