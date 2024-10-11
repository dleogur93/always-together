package projlee.modules.account.addressUpdate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;

@Controller
@RequiredArgsConstructor
public class AddressUpdateController {

    private final AccountService accountService;



    @GetMapping("/account/address")
    public String addressUpdateForm(@CurrentAccount Account account, Model model) {
        Account checkAccount = accountService.checkAccount(account.getAccountId());
        model.addAttribute(account);
        model.addAttribute(new AddressUpdateForm(checkAccount));

        return "account/address";

    }

    @PostMapping("/account/address")
    public String addressUpdate(@CurrentAccount Account account ,
                                @Valid AddressUpdateForm addressUpdateForm, Errors errors, Model model,
                                RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "account/address";
        }
        accountService.updateAddress(account, addressUpdateForm);
        attributes.addFlashAttribute("message", "주소를 변경했습니다.");
        return "redirect:/account/address";
    }
}
