package projlee.modules.account.phoneNumberUpdate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.CreateAccountForm;
import projlee.modules.account.addressUpdate.AddressUpdateForm;

@Component
@RequiredArgsConstructor
public class PhoneNumberUpdateFormValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PhoneNumberUpdateForm.class);
    }
    @Override
    public void validate(Object object, Errors errors) {

        PhoneNumberUpdateForm phoneNumberUpdateForm = (PhoneNumberUpdateForm) object;

        if (accountRepository.existsByPhoneNumber(phoneNumberUpdateForm.getNewPhoneNumber())) {
            errors.rejectValue("newPhoneNumber", "invalid.phoneNumber",  new Object[]{phoneNumberUpdateForm.getNewPhoneNumber()},"이미 사용중인 휴대번호입니다.");
        }

    }
}
