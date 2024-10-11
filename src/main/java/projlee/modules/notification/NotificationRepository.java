package projlee.modules.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.domain.Account;

import java.util.List;

@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countByAccountAndChecked(Account account, boolean checked);

    List<Notification> findByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account, boolean checked);

    void deleteByAccountAndChecked(Account account, boolean checked);
}
