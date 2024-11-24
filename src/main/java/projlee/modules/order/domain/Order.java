package projlee.modules.order.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.account.domain.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Long_id")
    private Account account;

    @OneToMany(mappedBy = "order",
                cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    private OrderStatus status;

//    ======= 연관관계 편의 메서드

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setAccount(Account account) {
        this.account = account;
        account.getOrders().add(this);
    }


    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // Creation logic
    public static Order createOrder(Account account, Delivery delivery, OrderItem... orderItems) {

        Order order = new Order();
        order.setAccount(account);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDateTime(LocalDateTime.now());
        return order;
    }

    // Order cancellation logic
    public void cancelOrder() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalArgumentException("이미 배송된 상품입니다.");
        }

        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public boolean containsItem(Long itemId) {
        // 주문 내 모든 상품 아이템을 확인하여 해당 상품이 포함되어 있는지 체크
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getItem().getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

}
