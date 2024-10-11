package projlee.modules.account;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import projlee.modules.account.domain.Account;

import java.util.List;
import java.util.Set;

@Getter
public class UserAccount extends User {

    private Account account;

    public UserAccount(Account account, Set<GrantedAuthority> authorities) {
        super(account.getAccountId(), account.getPassword(), authorities);
        this.account = account;
    }


}
