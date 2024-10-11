package projlee.modules.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.AccountService;
import projlee.modules.account.domain.Account;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.repository.DogRepository;
import projlee.modules.dog.service.DogService;
import projlee.modules.reservation.domain.DogReservation;
import projlee.modules.reservation.form.DogReservationForm;
import projlee.modules.reservation.repository.DogReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DogReservationService {

    private final DogRepository dogRepository;
    private final AccountRepository accountRepository;
    private final DogReservationRepository dogReservationRepository;


    public void createDogReservation( Long accountId, Long dogId, LocalDateTime localDateTime) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id"));

        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dog Id"));

        dog.reservationBoolean(); //예약 확정 로직
        account.dogReservationBoolean(); //

        dogRepository.save(dog);
        accountRepository.save(account);

        DogReservation dogReservation = DogReservation.builder()
                .account(account)
                .reservation(true)
                .dog(dog)
                .reservationTime(localDateTime)
                .reservationCreateTime(LocalDate.now())
                .build();


        dogReservationRepository.save(dogReservation);

    }

    public Boolean reservationFalse(Long accountId) {
      return dogReservationRepository.existsById(accountId);
    }

    public void delete(DogReservation dogReservation) {

        dogReservationRepository.delete(dogReservation);


    }

    public void accountDogReservationDelete(Account getAccount) {
        getAccount.accountDogReservationDelete();
    }

    public void dogReservationDelete(Dog getDog) {
        getDog.dogReservationDelete();
    }



}
