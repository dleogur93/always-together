package projlee.modules.reservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projlee.modules.reservation.domain.DogReservation;

public interface DogReservationRepositoryCustom {

    Page<DogReservation> findAll(Pageable pageable);

}
