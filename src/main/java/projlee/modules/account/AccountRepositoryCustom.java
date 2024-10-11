package projlee.modules.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projlee.modules.account.domain.Account;

import java.util.List;

public interface AccountRepositoryCustom {

    Page<Account> findAll(Pageable pageable);

    Page<Account> findByAccountId(Pageable pageable,String accountId);


}
