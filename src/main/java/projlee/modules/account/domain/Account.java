package projlee.modules.account.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.cart.Cart;
import projlee.modules.order.domain.Order;
import projlee.modules.reservation.domain.DogReservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id @GeneratedValue
    @Column(unique = true, name = "account_long_id")
    private Long id;

    @Column(unique = true)
    private String accountId;

    private String name;

    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Embedded
    private Address address;

    private LocalDateTime createdAt;

    private String code;


    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "account",
            fetch = FetchType.LAZY)
    private DogReservation dogReservation;

    private Boolean accountDogReservation;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private Cart cart;



    public void passwordUpdate(String newPassword) {
        this.password = newPassword;
    }

    public void phoneNumberUpdate(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public void roleModify(Role newRole) {
        this.role = newRole;
    }



    public void dogReservationBoolean(){
        accountDogReservation = true;
    }

    public void accountDogReservationDelete(){
        accountDogReservation = false;
        this.dogReservation = null;

    }

    public void setCart(Cart cart) {
        this.cart = cart;
        cart.setAccount(this);  // Account와 Cart 간의 양방향 관계 설정
    }

}
