package projlee.modules.order.form;

import lombok.Data;
import projlee.modules.order.domain.OrderStatus;

@Data
public class OrderSearch {

    private String accountName; //회원 이름
    private OrderStatus orderStatus; //주문 상태[ORDER, CANCEL]
}
