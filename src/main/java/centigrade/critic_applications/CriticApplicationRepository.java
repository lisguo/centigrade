package centigrade.critic_applications;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticApplicationRepository extends CrudRepository<CriticApplication, Long>{

    Iterable<CriticApplication> findAll();
    CriticApplication findCriticApplicationByAccount(long accountId);
    CriticApplication findCriticApplicationById(long id);
    CriticApplication save(CriticApplication ca);
    void delete(CriticApplication ca);
}
