package projlee.modules.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.domain.Account;
import projlee.modules.notification.Notification;
import projlee.modules.notification.NotificationRepository;
import projlee.modules.notification.NotificationType;
import projlee.modules.order.domain.Order;

import java.time.LocalDateTime;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class OrderReadyListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void onOrderReady(OrderReadyEvent orderReadyEvent) {
        Order order = orderReadyEvent.getOrder();
        Account account = order.getAccount();

        Notification notification = new Notification();
        notification.setTitle("배송준비가 완료되었습니다.");
        notification.setMessage(" 주문번호  '" + order.getId() + "' 배송준비 완료");
        notification.setChecked(false);
        notification.setAccount(account);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setNotificationType(NotificationType.DELIVERY_START);

        notificationRepository.save(notification);

//        log.info("알람을 보냈습니다. : " + account.getAccountId() + ", 주문번호 : " + order.getId());
    }
}
