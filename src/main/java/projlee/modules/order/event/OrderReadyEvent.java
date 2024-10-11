package projlee.modules.order.event;

import lombok.Getter;
import projlee.modules.order.domain.Order;

@Getter
public class OrderReadyEvent {

    private Order order;

    public OrderReadyEvent(Order order) {
        this.order = order;
    }
}
