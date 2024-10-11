package projlee.modules.account.forget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.account.AccountRepository;

@Component
@RequiredArgsConstructor
public class PasswordForgetValidator implements Validator {

   private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForgetForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {

        PasswordForgetForm passwordForgetForm = (PasswordForgetForm) object;


        if (!accountRepository.existsByEmailAndAccountId(passwordForgetForm.getEmail(), passwordForgetForm.getAccountId())) {
            errors.rejectValue("email","invalid.email.accountId","입력하신정보를 다시확인해주세요");
            errors.rejectValue("accountId","invalid.email.accountId","입력하신정보를 다시확인해주세요");
        }

    }
}
