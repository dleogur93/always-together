package projlee.modules.order.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.item.domain.Item;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    void setOrder(Order order) {
        this.order = order;
    }

    //////

    public void cancel() {
        getItem().addStockQuantity(count);
    }

    /**
     *  전체 가격
     */
    public int getTotalPrice() {
        return orderPrice * count;
    }

    /**
     * 생성 메서드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();

        item.removeStockQuantity(count);


        return orderItem;
    }
}
