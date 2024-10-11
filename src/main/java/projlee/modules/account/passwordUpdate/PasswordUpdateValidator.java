package projlee.modules.account.passwordUpdate;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import projlee.modules.account.CreateAccountForm;

@Component
public class PasswordUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(PasswordUpdateForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PasswordUpdateForm passwordUpdateForm = (PasswordUpdateForm) object;

        if (!passwordUpdateForm.getNewPassword().equals(passwordUpdateForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "wrong.value", "비밀번호가 일치하지 않습니다.");
        }

    }
}
