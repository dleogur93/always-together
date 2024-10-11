package projlee.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.AccountService;
import projlee.modules.account.CreateAccountForm;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.Address;
import projlee.modules.verificationCode.VerificationCodeRepository;
import projlee.modules.verificationCode.VerificationCodeService;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        VerificationCode verificationCode = VerificationCode.builder()
                .email("lee_0202@naver.com")
                .code("12345")
                .expirationTime(LocalDateTime.now())
                .build();
        verificationCodeRepository.save(verificationCode);

        Address address = new Address("1","2","3","4");

        CreateAccountForm createAccountForm = new CreateAccountForm();
        createAccountForm .setAccountId("qpslxhdl14");
        createAccountForm.setName("이대혁");
        createAccountForm.setPassword("1234");
        createAccountForm.setPhoneNumber("01059050399");
        createAccountForm.setEmail("lee_0202@naver.com");
        createAccountForm.setPostcode(address.getPostcode());
        createAccountForm.setAddress(address.getAddress());
        createAccountForm.setDetailAddress(address.getDetailAddress());
        createAccountForm.setExtraAddress(address.getExtraAddress());
        createAccountForm.setCode(verificationCode.getCode());



        accountService.accountSignup(createAccountForm);
    }


    @Test
    @DisplayName("main 화면 ")
    void mainView() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_ok() throws Exception {


        mockMvc.perform(post("/login")
                .param("accountId","qpslxhdl14")
                .param("password","1234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("이대혁"));
    }

    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("accountId", "qpslxhdl14")
                        .param("password", "000000000")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }


}