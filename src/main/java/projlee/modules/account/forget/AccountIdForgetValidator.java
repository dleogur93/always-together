package projlee.modules.account.forget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.CreateAccountForm;

@Component
@RequiredArgsConstructor
public class AccountIdForgetValidator implements Validator {

   private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountIdForgetForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        AccountIdForgetForm accountIdForgetForm = (AccountIdForgetForm) object;

        if (!accountRepository.existsByEmail(accountIdForgetForm.getEmail())) {
            errors.rejectValue("email","invalid.email","존재하는 이메일이 없습니다.");
        }
    }
}
