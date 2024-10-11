package projlee.modules.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.Address;
import projlee.modules.verificationCode.VerificationCodeRepository;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("회원가입 화면 보이는지")
    void signUpForm() throws Exception {

        mockMvc.perform(get("/account/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @Test
    @DisplayName("회원 가입 처리 오류")
    void account_signUp_error() throws Exception{
        mockMvc.perform(post("/account/sign-up")
                .param("postcode","1")
                .param("address","2")
                .param("detailAddress","3")
                .param("extraAddress","4")
                .param("accountId","qpslxhdl14")
                .param("name","이대혁")
                .param("password","1234")
                        .with(csrf())
                .param("email","lee_0202....")
                .param("phoneNumber","01050950399")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @Test
    @DisplayName("회원 가입 성공")
    void account_signUp_ok() throws Exception{


        VerificationCode verificationCode = VerificationCode.builder()
                .email("lee_0202@naver.com")
                .code("12345")
                .expirationTime(LocalDateTime.now())
                .build();
        verificationCodeRepository.save(verificationCode);

        Address address = new Address("1","2","3","4");

        Account account =Account.builder()
                .accountId("qpslxhdl14")
                .name("이대혁")
                .password("1234")
                .phoneNumber("01059050399")
                .email("lee_0202@naver.com")
                .address(address)
                .code(verificationCode.getCode())
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);


        mockMvc.perform(post("/account/sign-up")
                        .param("postcode","1")
                        .param("address","2")
                        .param("detailAddress","3")
                        .param("extraAddress","4")
                        .param("accountId","qpslxhdl14")
                        .param("name","이대혁")
                        .param("password","1234")
                        .with(csrf())
                        .param("email","lee_0202@naver.com")
                        .param("phoneNumber","01050950399")
                        .param("code","12345")

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/main"));

        Account account1 = accountRepository.findByAccountId("qpslxhdl14");
        assertNotNull(account1);
        assertNotEquals(account1.getPassword(),"1234");
//        assertEquals(account.getPassword(),"1234");


        assertTrue(accountRepository.existsByEmail("lee_0202@naver.com"));
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

}