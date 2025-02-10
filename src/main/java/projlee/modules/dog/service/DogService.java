package projlee.modules.dog.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.repository.DogRepository;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class DogService {

    private final DogRepository dogRepository;

    public List<Dog> firstPage() {
       return dogRepository.findTop12ByAdoptionFalseOrderByDogRegistrationDateDesc();
    }

    public Dog getDog(Long id) {

        return dogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dog not found "));
    }

    public Page<Dog> dogList(Pageable pageable) {

        return dogRepository.findAllByAdoptionFalse(pageable);

    }

    public List<Dog> findByDogBreed(String dogBreed) {

      return dogRepository.findByDogBreedWithReservation(dogBreed);
    }

    public List<Dog> findAll() {
        return dogRepository.findAll();
    }
}
