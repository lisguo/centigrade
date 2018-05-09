package centigrade.accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findAccountByEmail(String email);

    List<Account> findAll();

    Account findAccountById(long id);

    Account findAccountByNonce(String nonce);

    Account save(Account a);

    void delete(Account m);
}
