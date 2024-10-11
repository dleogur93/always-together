package projlee.modules.account.passwordUpdate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.domain.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PasswordUpdateControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @DisplayName("비밀번호 변경 성공")
    @Test
    void password_update_ok() throws Exception {

        mockMvc.perform(post("/account/password")
                .param("newPassword","12345")
                .param("newPasswordConfirm","12345")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/password"));

        Account qpslxhdl14 = accountRepository.findByAccountId("qpslxhdl14");
        assertTrue(passwordEncoder.matches("12345", qpslxhdl14.getPassword()));
    }
}