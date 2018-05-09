package centigrade.reported_accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportedAccountService {
    @Autowired
    ReportedAccountRepository reportedAccountRepository;

    public List<ReportedAccount> getAllReportedAccounts(){ return reportedAccountRepository.findAll(); }

    public void deleteReportedAccount(ReportedAccount ra) { reportedAccountRepository.delete(ra); }

    public ReportedAccount getReportedAccountById(long id) { return reportedAccountRepository.findReportedAccountByid(id); }

    public void saveReportedAccount(ReportedAccount ra){
        reportedAccountRepository.save(ra);
    }
}
