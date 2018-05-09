package centigrade.reported_accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedAccountRepository extends CrudRepository<ReportedAccount, Long>{
    List<ReportedAccount> findAll();
    void delete(ReportedAccount ra);
    ReportedAccount save(ReportedAccount ra);
    ReportedAccount findReportedAccountByid(long id);
}
