package projlee.modules.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import projlee.modules.account.domain.Account;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByAccount(Account account);


}
