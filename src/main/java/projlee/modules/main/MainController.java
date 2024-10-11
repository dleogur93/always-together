package projlee.modules.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.banner.Banner;
import projlee.modules.banner.BannerService;
import projlee.modules.dog.service.DogService;
import projlee.modules.item.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DogService dogService;
    private final ItemService itemService;
    private final BannerService bannerService;


    @GetMapping("/")
    public String main(@CurrentAccount Account account, Model model){

        if (bannerService.findOne() != null) {
            model.addAttribute("banner",bannerService.findOne());
        } else {
            Banner banner = new Banner();
            banner.setBannerImage("/images/default-banner.png");
            banner.setBannerName("Default Banner");
            model.addAttribute("banner", banner);
        }


        if (account != null) {
            model.addAttribute(account);
            model.addAttribute("dogList", dogService.firstPage());
            model.addAttribute("itemList",itemService.itemFirstPage());

            return "index";
        }


        model.addAttribute("itemList",itemService.itemFirstPage());
        model.addAttribute("dogList", dogService.firstPage());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }
}
