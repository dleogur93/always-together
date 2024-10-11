package projlee.modules.account;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.QAccount;

import java.util.List;

public class AccountRepositoryImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom{

    public AccountRepositoryImpl() {
        super(Account.class);
    }


    @Override
    public Page<Account> findAll(Pageable pageable) {
        QAccount account = QAccount.account;

       JPQLQuery<Account> query = from(account)
               .distinct();
        JPQLQuery<Account> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Account> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(),pageable, fetchResults.getTotal());

    }

    @Override
    public Page<Account> findByAccountId(Pageable pageable, String accountId) {
        QAccount account = QAccount.account;

        JPQLQuery<Account> query = from(account)
                .where(account.accountId.containsIgnoreCase(accountId))
                .distinct();

        JPQLQuery<Account> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Account> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(),pageable, fetchResults.getTotal());
    }
}
