package projlee.modules.dog.form;

import jakarta.persistence.Column;
import lombok.Data;
import projlee.modules.dog.domain.Sex;

@Data
public class DogRegistration {

    private String dogBreed; //개 품종

    private String dogName;

    private int dogAge;

    private Sex dogSex;

    private String dogPicture;

    private String information;
}
