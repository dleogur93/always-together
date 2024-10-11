package projlee.modules.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;

    @GetMapping("/account/notifications")
    public String getNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
        Long numberOfChecked = notificationRepository.countByAccountAndChecked(account, true);
        putCategorizedNotifications(model, notifications, numberOfChecked, notifications.size());
        model.addAttribute("isNew", true);
        notificationService.markAsRead(notifications);
        return "account/notifications";
    }

    @GetMapping("/account/notifications/old")
    public String getOldNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
        Long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account, false);
        putCategorizedNotifications(model, notifications,  notifications.size(),numberOfNotChecked);
        model.addAttribute("isNew", false);
        return "account/notifications";
    }

    @PostMapping("/account/notifications")
    public String deleteNotifications(@CurrentAccount Account account) {
        notificationRepository.deleteByAccountAndChecked(account, true);
        return "redirect:/account/notifications";
    }


    private void putCategorizedNotifications(Model model, List<Notification> notifications,
                                             long numberOfChecked, long numberOfNotChecked) {
        List<Notification> deliveryReadys = new ArrayList<>();

        for (var notification : notifications) {
            switch (notification.getNotificationType()) {
                case DELIVERY_START: deliveryReadys.add(notification); break;
            }
        }

        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("deliveryReadys", deliveryReadys);

    }
}
