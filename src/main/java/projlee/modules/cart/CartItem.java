package projlee.modules.cart;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.item.domain.Item;

@Entity
@Table(name = "cart_items")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int count;

    void setCart(Cart cart) {
        this.cart = cart;
    }

    //생성메서드
    public static CartItem createCartItem(Item item,int count) {
        return CartItem.builder()
                .item(item)
                .count(count)
                .build();
    }



}
