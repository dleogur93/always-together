package projlee.modules.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.Role;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AccountRepository accountRepository;

    public Page<Account> accountList(Pageable pageable) {

        return accountRepository.findAll(pageable);
    }

    public Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId);

    }

    public void roleModify(String accountId, Role newRole) {

        Account account = accountRepository.findByAccountId(accountId);
        account.roleModify(newRole);
        accountRepository.save(account);

    }

//    public Account findByAccountId(String accountId) {
//       return accountRepository.findByAccountId(accountId);
//    }

    public Page<Account> searchAccountsByAccountId(Pageable pageable, String accountId) {
        return accountRepository.findByAccountId(pageable, accountId);
    }
}
