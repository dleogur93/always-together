package projlee.modules.dog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projlee.modules.dog.domain.Dog;

public interface DogRepositoryCustom {

    Page<Dog> findAllByAdoptionFalse(Pageable pageable);


}

