package projlee.modules.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.domain.Account;
import projlee.modules.account.forget.AccountIdForgetForm;
import projlee.modules.account.forget.AccountIdForgetValidator;
import projlee.modules.account.forget.PasswordForgetForm;
import projlee.modules.account.forget.PasswordForgetValidator;
import projlee.modules.account.passwordUpdate.PasswordUpdateForm;
import projlee.modules.verificationCode.VerificationCodeService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final CreateAccountValidator accountValidator;

    private final AccountIdForgetValidator accountIdForgetValidator;

    private final PasswordForgetValidator passwordForgetValidator;

    private final VerificationCodeService verificationCodeService;


    private final JavaMailSender javaMailSender;



    @InitBinder("createAccountForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountValidator);

    }

    @InitBinder("accountIdForgetForm")
    public void accountIdForgetFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountIdForgetValidator);

    }

    @InitBinder("passwordForgetForm")
    public void passwordForgetFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordForgetValidator);
    }



    @GetMapping("/account/sign-up")
    public String createAccount(Model model) {

        model.addAttribute(new CreateAccountForm());

        return "account/sign-up";
    }



    @PostMapping("/account/sign-up")
    public String createAccount(@Valid CreateAccountForm createAccountForm,
                                BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "account/sign-up";
        }

        if (accountService.verifyCode(createAccountForm.getEmail(), createAccountForm.getCode())) {
           accountService.accountSignup(createAccountForm);
           verificationCodeService.deleteCode();

            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid verification code.");
            return "account/sign-up";
        }

    }
////////////////////////////////////////////
    @PostMapping("/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam(name = "email") String email) {
        return accountService.isEmailUnique(email) ? "OK" : "Duplicated";

    }

    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public String sendVerificationCode(@Valid CreateAccountForm createAccountForm) {
        return accountService.sendVerificationCode(createAccountForm);
    }

    //////////////////////////////
    /**
     * 마이페이지
     */

    @GetMapping("/account/myPage")
    public String myPage( Model model, @CurrentAccount Account account ) {

        Account accountMyPage = accountService.getAccount(account.getAccountId());

        model.addAttribute(accountMyPage);
        model.addAttribute("isOwner", accountMyPage.equals(account));
        return "account/myPage";
    }

    /**
     * 회원 탈퇴
     */

    @GetMapping("/account/delete")
    public String accountDeleteForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);

        return "account/delete";
    }

    @PostMapping("/account/delete")
    public String accountDelete(@CurrentAccount Account account) {

        accountService.accountDelete(account);

        return "redirect:/logout";
    }

    /**
     *  아이디 잊어버릴때
     */

    @GetMapping("/account/idForget")
    public String accountForgetForm(Model model) {

        model.addAttribute(new AccountIdForgetForm());

        return "account/idForget";
    }

    @PostMapping("/account/idForget")
    public String accountForget(@ModelAttribute @Valid AccountIdForgetForm accountIdForgetForm,
                                Errors errors, Model model, RedirectAttributes attributes) {


        if (errors.hasErrors()) {
            model.addAttribute(errors);
            return "account/idForget";
        }


        accountService.sendAccountId(accountIdForgetForm);
        attributes.addFlashAttribute("message", "이메일을 보냈습니다. 이메일을 확인해주세요");

        return "redirect:/account/idForget";
    }

    @GetMapping("account/passwordForget")
    public String passwordForgetForm(Model model) {

        model.addAttribute(new PasswordForgetForm());

        return "account/passwordForget";
    }

    @PostMapping("/account/passwordForget")
    public String passwordForget(@ModelAttribute @Valid PasswordForgetForm PasswordForgetForm,
                                Errors errors, Model model, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            model.addAttribute(errors);
            return "account/passwordForget";
        }


        accountService.sendPasswordId(PasswordForgetForm);
        attributes.addFlashAttribute("message", "이메일을 보냈습니다. 이메일을 확인해주세요");

        return "redirect:/account/passwordForget";
    }

    ////////////////

}
