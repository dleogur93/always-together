package projlee.modules.dog.domain;

import jakarta.persistence.*;
import lombok.*;
import projlee.modules.manager.form.DogModifyForm;
import projlee.modules.reservation.domain.DogReservation;

import java.time.LocalDate;

@Entity
@Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dog {

    @Id
    @GeneratedValue
    @Column(name = "dog_id")
    private Long id;

    private String dogBreed; //개 품종

    @Column(unique = true)
    private String dogName;

    private int dogAge;

    @Enumerated(EnumType.STRING)
    private Sex dogSex;

    private boolean reservation;

    private boolean adoption;

    private String adopterName; //입양자 이름


    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "LONGTEXT")
    private String dogPicture;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "LONGTEXT")
    private String information;

    private LocalDate dogRegistrationDate;




    public void dogModify(DogModifyForm dogModifyForm) {
        this.dogBreed = dogModifyForm.getDogBreed();
        this.dogName = dogModifyForm.getDogName();
        this.dogAge = dogModifyForm.getDogAge();
        this.dogSex = dogModifyForm.getDogSex();
        this.information = dogModifyForm.getInformation();
        this.dogPicture = dogModifyForm.getDogPicture();
        this.dogRegistrationDate = LocalDate.now();
    }


    @OneToOne(mappedBy = "dog",
            fetch = FetchType.LAZY)
    private DogReservation dogReservation;

    public void reservationBoolean(){
        this.reservation = true;
    }

    public void dogReservationDelete(){
        this.reservation = false;
        this.dogReservation = null;
    }

    public void adoptionConfirmed(String adopterName){
        this.adoption = true;
        this.adopterName= adopterName;
    }

}
