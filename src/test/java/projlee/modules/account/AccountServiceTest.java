package projlee.modules.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projlee.modules.account.domain.Account;
import projlee.modules.verificationCode.VerificationCodeRepository;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;


    @Autowired
    private AccountService accountService;

    @BeforeEach
    void clean() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Account 회원 가입 성공")
    void accountSignupOk() {


        VerificationCode verificationCode = VerificationCode.builder()
                .email("lee_0202@naver.com")
                .code("1234")
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .build();

        verificationCodeRepository.save(verificationCode);


        CreateAccountForm createAccountForm = new CreateAccountForm();
        createAccountForm.setAccountId("lee_0202");
        createAccountForm.setName("이대혁");
        createAccountForm.setPassword("123456");
        createAccountForm.setEmail("lee_0202@naver.com");
        createAccountForm.setPhoneNumber("010123456789");
        createAccountForm.setPostcode("111");
        createAccountForm.setAddress("222");
        createAccountForm.setDetailAddress("333");
        createAccountForm.setExtraAddress("444");
        createAccountForm.setCode("1234");


        accountService.accountSignup(createAccountForm);

        Account findAccountId = accountRepository.findByAccountId("lee_0202");

        assertEquals(1,accountRepository.count());
        assertThat(findAccountId.getAccountId()).isEqualTo("lee_0202");
        assertThat(findAccountId.getAddress().getAddress()).isEqualTo("222");


    }

    @Test
    @DisplayName("가입 실패 - 아이디 중복")
    void accountSignupFail() {


        VerificationCode verificationCode = VerificationCode.builder()
                .email("lee_0202@naver.com")
                .code("1234")
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .build();

        verificationCodeRepository.save(verificationCode);

        CreateAccountForm createAccountForm = new CreateAccountForm();
        createAccountForm.setAccountId("lee_0202");
        createAccountForm.setName("이대혁");
        createAccountForm.setPassword("123456");
        createAccountForm.setEmail("lee_0202@naver.com");
        createAccountForm.setPhoneNumber("010123456789");
        createAccountForm.setPostcode("111");
        createAccountForm.setAddress("222");
        createAccountForm.setDetailAddress("333");
        createAccountForm.setExtraAddress("444");
        createAccountForm.setCode("1234");

        accountService.accountSignup(createAccountForm);

        ///////////////////////////////

        VerificationCode verificationCode2 = VerificationCode.builder()
                .email("lee_02023@naver.com")
                .code("1234")
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .build();

        verificationCodeRepository.save(verificationCode2);

        CreateAccountForm createAccountForm2 = new CreateAccountForm();
        createAccountForm2.setAccountId("lee_0202");
        createAccountForm2.setName("이대혁");
        createAccountForm2.setPassword("123456");
        createAccountForm2.setEmail("lee_02023@naver.com");
        createAccountForm2.setPhoneNumber("0101234567892");
        createAccountForm2.setPostcode("111");
        createAccountForm2.setAddress("222");
        createAccountForm2.setDetailAddress("333");
        createAccountForm2.setExtraAddress("444");
        createAccountForm2.setCode("1234");


        assertThatThrownBy(() -> accountService.accountSignup(createAccountForm2))
                        .isInstanceOf(Exception.class);

        assertThat(1).isEqualTo(accountRepository.count());
    }


}