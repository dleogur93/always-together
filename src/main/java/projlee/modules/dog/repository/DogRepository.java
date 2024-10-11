package projlee.modules.dog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.dog.domain.Dog;

import java.util.List;

@Transactional(readOnly = true)
public interface DogRepository extends JpaRepository<Dog, Long> ,DogRepositoryCustom{

    boolean existsByDogName(String dogName);

    Page<Dog> findByDogName(Pageable pageable, String dogName);

    List<Dog> findTop12ByAdoptionFalseOrderByDogRegistrationDateDesc();


    Page<Dog> findByAdoptionTrue(Pageable pageable);


    List<Dog> findByDogBreed(String dogBreed);
}
