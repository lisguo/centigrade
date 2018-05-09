package centigrade.reported_accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportedAccountService {
    @Autowired
    ReportedAccountRepository reportedAccountRepository;

    public void deleteAllReportedAccounts(long userId){
        List<ReportedAccount> reportedAccounts = getAllReportedAccountsByUser(userId);
        for(ReportedAccount ra : reportedAccounts){
            deleteReportedAccount(ra);
        }
    }

    public List<ReportedAccount> getAllReportedAccountsByUser(long userId) { return reportedAccountRepository.findReportedAccountsByReportedId(userId);}

    public List<ReportedAccount> getAllReportedAccounts(){ return reportedAccountRepository.findAll(); }

    public void deleteReportedAccount(ReportedAccount ra) { reportedAccountRepository.delete(ra); }

    public ReportedAccount getReportedAccountById(long id) { return reportedAccountRepository.findReportedAccountById(id); }

    public void saveReportedAccount(ReportedAccount ra){
        reportedAccountRepository.save(ra);
    }
}
