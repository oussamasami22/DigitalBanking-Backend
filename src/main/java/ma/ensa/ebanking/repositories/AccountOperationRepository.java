package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccountId(Long accountId);

    Page<AccountOperation> findByBankAccountId(String accountId,Pageable pageable);
}
