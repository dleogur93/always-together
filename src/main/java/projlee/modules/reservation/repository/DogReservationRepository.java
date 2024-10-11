package projlee.modules.reservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.reservation.domain.DogReservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface DogReservationRepository extends JpaRepository<DogReservation, Long> , DogReservationRepositoryCustom {


    Page<DogReservation> findDogReservationsByReservationTime(LocalDateTime dateTime, Pageable pageable);

    List<DogReservation> findByReservationTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
