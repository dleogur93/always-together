package projlee.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.account.passwordUpdate.PasswordUpdateForm;
import projlee.modules.verificationCode.VerificationCodeRepository;

@Component
@RequiredArgsConstructor
public class CreateAccountValidator implements Validator {


    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateAccountForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {

        CreateAccountForm createAccountForm = (CreateAccountForm) object;

        if (accountRepository.existsByEmail(createAccountForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{createAccountForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (accountRepository.existsByAccountId(createAccountForm.getAccountId())) {
            errors.rejectValue("accountId", "invalid.accountId", new Object[]{createAccountForm.getAccountId()}, "이미 사용중인 아이디입니다.");
        }

        if (accountRepository.existsByPhoneNumber(createAccountForm.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "invalid.phoneNumber", new Object[]{createAccountForm.getPhoneNumber()}, "이미 사용중인 휴대번호입니다.");
        }

    }
}
