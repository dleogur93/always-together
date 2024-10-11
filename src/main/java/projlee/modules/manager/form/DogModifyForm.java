package projlee.modules.manager.form;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import projlee.modules.dog.domain.Dog;
import projlee.modules.dog.domain.Sex;

@Data
@NoArgsConstructor
public class DogModifyForm {

    private String dogBreed;

    @Column(unique = true)
    private String dogName;

    private int dogAge;

    private Sex dogSex;

    private String dogPicture;

    private String information;

    public DogModifyForm(Dog dog) {
        this.dogBreed = dog.getDogBreed();
        this.dogName =  dog.getDogName();
        this.dogAge =  dog.getDogAge();
        this.dogSex =  dog.getDogSex();
        this.dogPicture =  dog.getDogPicture();
        this.information =  dog.getInformation();
    }



}
