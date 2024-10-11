package projlee.modules.account.passwordUpdate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;

@Controller
@RequiredArgsConstructor
public class PasswordUpdateController {

    private final PasswordUpdateValidator passwordUpdateValidator;

    private final AccountService accountService;

    @InitBinder("passwordUpdateForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordUpdateValidator);
    }

    @GetMapping("/account/password")
    public String passwordUpdateForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(new PasswordUpdateForm());

        return "account/password";
    }

    @PostMapping("/account/password")
    public String passwordUpdate(@CurrentAccount Account account, @Valid PasswordUpdateForm passwordUpdateForm,
                                 Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "account/password";
        }

        accountService.updatePassword(account, passwordUpdateForm.getNewPassword());
        attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
        return "redirect:/account/password";
    }
}
