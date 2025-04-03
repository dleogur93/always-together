package projlee.modules.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.domain.Account;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    boolean existsByEmail(String email);

    boolean existsByAccountId(String accountId);

    Account findByAccountId(String accountId);

    Optional<Account> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndAccountId(String email, String accountId);

//    @EntityGraph(attributePaths = {"cart", "dogReservation", "orders"})
//    Page<Account> findAll(Pageable pageable);

}
