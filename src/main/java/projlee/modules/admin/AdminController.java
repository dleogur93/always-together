package projlee.modules.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projlee.modules.account.AccountService;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.Role;
import projlee.modules.admin.form.RoleForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/admin/membershipManagement")
    public String membershipManagement(@PageableDefault(size = 9, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
                                       @RequestParam(name = "accountId", required = false) String accountId , Model model) {


        Page<Account> accounts;
        if (accountId != null && !accountId.isEmpty()) {
            accounts = adminService.searchAccountsByAccountId(pageable,accountId);
        } else {
            accounts = adminService.accountList(pageable);
        }

        String sortProperty = "id";
        String sortDirection = "desc";

        for (Sort.Order order : pageable.getSort()) {
            sortProperty = order.getProperty();
            sortDirection = order.getDirection().name().toLowerCase();
            break;
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("sortProperty", sortProperty);
        model.addAttribute("sortDirection", sortDirection);


        return "admin/membershipManagement";
    }


//        model.addAttribute("accounts",accounts);
//        model.addAttribute("sortProperty",pageable.getSort().toString().contains("id") ? "id" : "name");


    @GetMapping("/admin/memberInformation/{accountId}")
    public String adminMemberDetail(@PathVariable("accountId") String accountId ,Model model){

        Account account = adminService.getAccount(accountId);

        model.addAttribute("account",account);

        return "admin/memberInformation";

    }

    @GetMapping("/admin/roleModify/{accountId}")
    public String roleModifyForm(@PathVariable("accountId") String accountId , Model model,RoleForm newRoleForm) {

        Account account = adminService.getAccount(accountId);
        model.addAttribute(newRoleForm);
        model.addAttribute(account);

        return "admin/roleModify";
    }


    @PostMapping("/admin/roleModify/{accountId}")
    public String roleModify(@PathVariable("accountId") String accountId , Model model, RoleForm newRoleForm
                        , Errors errors, RedirectAttributes attributes) {

        Account account = adminService.getAccount(accountId);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "admin/roleModify";
        }

        model.addAttribute(account);
        adminService.roleModify(accountId,newRoleForm.getRole());
        attributes.addFlashAttribute("message","권한을 변경했습니다.");
        return "redirect:/admin/roleModify/{accountId}";

    }

}
