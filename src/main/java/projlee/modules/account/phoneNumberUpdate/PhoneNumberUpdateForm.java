package projlee.modules.account.phoneNumberUpdate;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import projlee.modules.account.domain.Account;

@Data
@NoArgsConstructor
public class PhoneNumberUpdateForm {

    @Pattern(regexp = "^01([0])-?([0-9]{3,4})-?([0-9]{4})$" , message = "올바른 휴대폰 번호 형식을 입력해주세요 :  예) 01012345678")
    private String newPhoneNumber;

    public PhoneNumberUpdateForm(Account account) {
        this.newPhoneNumber = account.getPhoneNumber();
    }
}
