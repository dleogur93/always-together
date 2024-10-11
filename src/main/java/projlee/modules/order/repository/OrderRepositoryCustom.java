package projlee.modules.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projlee.modules.account.domain.Account;
import projlee.modules.order.domain.Order;
import projlee.modules.order.form.OrderSearch;

public interface OrderRepositoryCustom {

    Page<Order> findSearchOrder(Pageable pageable, OrderSearch orderSearch);

    Page<Order> accountCancelOrder(Pageable pageable, Account account);

    Page<Order> accountCompleteDelivery(Pageable pageable, Account account);

    Page<Order> OrderDeliveryList(Pageable pageable);

    Page<Order> OrderCancelList(Pageable pageable);

    Page<Order> OrderCompleteList(Pageable pageable);

    Page<Order> orderDeliveryList(Pageable pageable, Account account);
}
