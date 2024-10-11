package projlee.modules.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.item.domain.Item;
import projlee.modules.item.service.ItemService;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final AccountService accountService;
    private final CartService cartService;
    private final ItemService itemService;

    @GetMapping("/account/cart")
    public String cart(@CurrentAccount Account account, Model model) {

        Account getAccount = accountService.getAccount(account.getAccountId());
        Cart cart = cartService.getCart(account);

        int totalPrice = 0;
        if (cart != null) {
            totalPrice = cartService.calculateTotalPrice(cart);
        }


        model.addAttribute("cart",cart);
        model.addAttribute("account", getAccount);
        model.addAttribute("totalPrice", totalPrice);


        return "account/cart";
    }

    @PostMapping("/account/cart/add")
    public String cartAdd(@CurrentAccount Account account,
                          @RequestParam("itemId") Long itemId,
                          @RequestParam("count") int count) {

        Account getAccount = accountService.getAccount(account.getAccountId());
        Item item = itemService.findById(itemId);
        cartService.addCart(getAccount,item.getId(),count);

        return "redirect:/account/cart";
    }

    @PostMapping("/account/cart/remove")
    public String cartRemove(@CurrentAccount Account account,
                             @RequestParam("cartItemId") Long cartItemId) {

        Account getAccount = accountService.getAccount(account.getAccountId());
        cartService.removeItemFromCart(getAccount,cartItemId);
        return "redirect:/account/cart";
    }


}
