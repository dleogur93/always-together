package projlee.modules.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.account.domain.Account;
import projlee.modules.dog.domain.Dog;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access =AccessLevel.PROTECTED)
public class DogReservation {

    @Id @GeneratedValue
    @Column(name = "dogReservation_id")
    private Long id;

    @Builder.Default
    private boolean reservation = false;

    private LocalDateTime reservationTime; //예약 날짜

    private LocalDate reservationCreateTime ; //예약 생성 시간


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;



}
