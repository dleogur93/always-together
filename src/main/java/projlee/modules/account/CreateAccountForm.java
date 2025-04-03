package projlee.modules.account;

import jakarta.persistence.Column;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class CreateAccountForm {


    @Column(unique = true)
    @Length(min = 3, max = 20)
    private String accountId;

    @Length(min = 2, max = 10)
    private String name;

    @Length(min = 8, max = 50)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "영문, 숫자, 특수기호를 포함해 8자리 ~ 50자리까지 입력해주세요")
    private String password;


    @Column(unique = true)
    @Pattern(regexp = "^01([0])-?([0-9]{3,4})-?([0-9]{4})$" , message = "올바른 휴대폰 번호 형식을 입력해주세요 :  예) 01012345678")
    private String phoneNumber;


    @Email
    @Column(unique = true)
    private String email;

    private String code;


    private String postcode;  //우편번호
    private String address;
    private String detailAddress;
    private String extraAddress;



}
