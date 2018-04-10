package centigrade.accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long>{

    Account findAccountByEmail(String email);
    Account findAccountById(long id);
    Account findAccountByNonce(String nonce);
    Account save(Account a);
    void delete(Account m);
}
