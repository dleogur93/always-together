package projlee.modules.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projlee.modules.account.domain.Account;
import projlee.modules.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> ,OrderRepositoryCustom {

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.item")
    Page<Order> findAllWithItems(Pageable pageable);

    Page<Order> findByAccount(Pageable pageable, Account account);
}
