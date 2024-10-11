package projlee.modules.account.phoneNumberUpdate;

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
import projlee.modules.account.addressUpdate.AddressUpdateForm;
import projlee.modules.account.domain.Account;
import projlee.modules.account.passwordUpdate.PasswordUpdateForm;

@Controller
@RequiredArgsConstructor
public class PhoneNumberUpdateController {

    private final AccountService accountService;

    private final PhoneNumberUpdateFormValidator phoneNumberUpdateFormValidator;

    @InitBinder("phoneNumberUpdateForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(phoneNumberUpdateFormValidator);
    }


    @GetMapping("/account/phoneNumber")
    public String phoneNumberUpdateForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(new PhoneNumberUpdateForm(account));

        return "account/phoneNumber";
    }

    @PostMapping("/account/phoneNumber")
    public String passwordUpdate(@CurrentAccount Account account, @Valid PhoneNumberUpdateForm phoneNumberUpdateForm,
                                 Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "account/phoneNumber";
        }

        accountService.updatePhoneNumber(account, phoneNumberUpdateForm.getNewPhoneNumber());
        attributes.addFlashAttribute("message", "핸드폰 번호를 변경했습니다.");
        return "redirect:/account/phoneNumber";
    }
}
