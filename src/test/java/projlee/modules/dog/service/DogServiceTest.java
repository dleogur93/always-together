package projlee.modules.dog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projlee.modules.dog.form.DogRegistration;
import projlee.modules.dog.repository.DogRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DogServiceTest {

    @Autowired
    private DogService dogService;

    @Autowired
    private DogRepository dogRepository;

//    @BeforeEach
//    void clean() {
//        dogRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("Dog 등록 성공")
//    void dogRegistration(){
//        DogRegistration dog = new DogRegistration();
//
//        dog.setDogBreed("멍멍이");
//        dog.setDogName("멍");
//        dog.setDogAge(10);
//        dog.setInformation("dsfefe");
//        dog.setDogPicture("picture");
//
//        dogService.dogRegistration(dog);
//
//        assertEquals(1,dogRepository.count());
//    }

//    @Test
//    @DisplayName("Dog 등록 실패")
//    void dogRegistration_false(){
//
//        DogRegistration dog = new DogRegistration();
//
//        dog.setDogBreed("멍멍이");
//        dog.setDogName("멍");
//        dog.setAge(10);
//        dog.setInformation("dsfefe");
//        dog.setDogPicture("picture");
//
//        dogService.dogRegistration(dog);
//
//
//        DogRegistration dog2 = new DogRegistration();
//
//        dog2.setDogBreed("멍멍이");
//        dog2.setDogName("멍");
//        dog2.setAge(10);
//        dog2.setInformation("dsfefe");
//        dog2.setDogPicture("picture");
//
//        dogService.dogRegistration(dog2);
//
//        assertEquals(1,dogRepository.count());
//    }

}