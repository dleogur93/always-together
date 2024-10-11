package projlee.modules.order.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.account.domain.Address;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",
            fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    void setOrder(Order order) {
        this.order = order;
    }

    public void deliveryStatusReady(){
        this.status = DeliveryStatus.READY;
    }

    public void deliveryStatusComp() {
        this.status = DeliveryStatus.COMP;
    }

}
