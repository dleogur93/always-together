package projlee.modules.account.passwordUpdate;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordUpdateForm {

    @Length(min = 8, max = 50)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$" ,
            message = "영문, 숫자, 특수기호를 포함해 8자리 ~ 50자리까지 입력해주세요")
    private String newPassword;

    private String newPasswordConfirm;


}
