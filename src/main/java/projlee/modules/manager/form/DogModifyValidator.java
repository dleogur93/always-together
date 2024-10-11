package projlee.modules.manager.form;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.dog.repository.DogRepository;

@Component
@RequiredArgsConstructor
public class DogModifyValidator implements Validator {

    private final DogRepository dogRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DogModifyForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {

        DogModifyForm dogModifyForm = (DogModifyForm) object;

        if (dogRepository.existsByDogName(dogModifyForm.getDogName())) {
            errors.rejectValue("dogName", "invalid.dog", new Object[]{dogModifyForm.getDogName()}, "이미 사용중인 유기견 이름입니다.");
        }


    }
}
