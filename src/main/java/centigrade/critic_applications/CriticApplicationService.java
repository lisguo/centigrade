package centigrade.critic_applications;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriticApplicationService {
    @Autowired
    CriticApplicationRepository applicationRepository;

    @Autowired
    AccountService accountService;

    public CriticApplication submitCriticApplication(long accountId, String sourceName, String sourceUrl, String resumeText){
        CriticApplication application = new CriticApplication();
        application.setAccount(accountId);
        application.setSourceName(sourceName);
        application.setSourceUrl(sourceUrl);
        application.setResumeText(resumeText);

        applicationRepository.save(application);
        return application;
    }

    public Iterable<CriticApplication> getAllApplications(){
        return applicationRepository.findAll();
    }

    public boolean exists(long accountId){
        CriticApplication application = applicationRepository.findCriticApplicationByAccount(accountId);
        if(application != null){
            return true;
        }
        return false;
    }

    public void setAccountInfo(Iterable<CriticApplication> applications){
        for(CriticApplication application : applications){
            Account a = accountService.getAccountById(application.getAccount());
            application.setFirstName(a.getFirstName());
            application.setLastName(a.getLastName());
            application.setEmail(a.getEmail());
        }
    }
}
