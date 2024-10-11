package projlee.modules.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projlee.modules.account.AccountService;
import projlee.modules.account.CurrentAccount;
import projlee.modules.account.domain.Account;
import projlee.modules.cart.Cart;
import projlee.modules.cart.CartService;
import projlee.modules.item.domain.Item;
import projlee.modules.item.service.ItemService;
import projlee.modules.order.domain.Order;
import projlee.modules.order.form.OrderSearch;
import projlee.modules.order.service.OrderService;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final AccountService accountService;
    private final CartService cartService;

    @GetMapping("/order")
    public String orderForm(@CurrentAccount Account account, @RequestParam("itemId") Long id, Model model) {

        Item item = itemService.findById(id);

        if (account != null) {
            model.addAttribute("account", account);
            model.addAttribute("item", item);

            return "order/orderForm";
        }

            return "index";
    }

    @PostMapping("/order")
    public String orderSubmit(
                            @CurrentAccount Account account,
                              @RequestParam("itemId") Long itemId,
                              @RequestParam("accountId") Long accountId,
                              @RequestParam("count") int count,
                              @PageableDefault(size = 9, direction = Sort.Direction.DESC)Pageable pageable,Model model) {

       orderService.order(accountId, itemId, count);

        Page<Order> orders = orderService.findByAccount(pageable, account);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

       return "redirect:/account/orderList";
    }

    @GetMapping("/account/cartOrderForm")
    public String cartOrderForm(@CurrentAccount Account account, Model model) {
        Cart cart = cartService.getCart(account);
        if (cart == null || cart.getCartItems().isEmpty()) {
            return "redirect:/account/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("account", account);
        return "order/orderFromCartForm";
    }

    @PostMapping("/account/cart/order")
    public String orderFromCart(@CurrentAccount Account account) {
        orderService.orderFromCart(account);
        return "redirect:/account/orderList";
    }


    @GetMapping("/manager/orders")
    public String accountOrders(@CurrentAccount Account account, @ModelAttribute("orderSearch") OrderSearch orderSearch,
                                @PageableDefault(size = 9,direction = Sort.Direction.DESC)Pageable pageable,
                                 Model model) {

        Page<Order> orders;

        orders = orderService.findSearchOrder(pageable,orderSearch);


        model.addAttribute("account", account);
        model.addAttribute("orders", orders);


        return "order/ordersManager";
    }

    @GetMapping("/manager/orders/deliveryList")
    public String orderDeliveryList(@CurrentAccount Account account,@PageableDefault(size = 9, direction = Sort.Direction.DESC)Pageable pageable,
                                    Model model) {


        Page<Order> orders = orderService.findByOrderDelivery(pageable);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "manager/orderDeliveryList";
    }

    @GetMapping("/manager/orders/cancelList")
    public String orderCancelList(@CurrentAccount Account account,@PageableDefault(size = 9, direction = Sort.Direction.DESC)Pageable pageable,
                                    Model model) {


        Page<Order> orders = orderService.findByOrderCancelList(pageable);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "manager/orderCancelList";
    }

    @GetMapping("/manager/orders/completeList")
    public String orderCompleteList(@CurrentAccount Account account,@PageableDefault(size = 9, direction = Sort.Direction.DESC)Pageable pageable,
                                  Model model) {


        Page<Order> orders = orderService.findByOrderCompleteList(pageable);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "manager/orderCompleteList";
    }

    @PostMapping("/manager/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/manager/orders";
    }

    @PostMapping("/manager/orders/{orderId}/ready")
    public String readyOrder(@PathVariable("orderId") Long orderId) {
        orderService.readyOrder(orderId);

        return "redirect:/manager/orders";
    }



    ////////////////////
    @GetMapping("/account/orderList")
    public String accountOrderList(@CurrentAccount Account account,
                                @PageableDefault(size = 9,sort = "orderDateTime",direction = Sort.Direction.DESC)Pageable pageable,Model model) {

        Page<Order> orders = orderService.findByAccount(pageable, account);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "account/orders";
    }

    @PostMapping("/account/orders/{orderId}/cancel")
    public String accountCancelOrder(
            @CurrentAccount Account account,
            @PathVariable("orderId") Long orderId ,Model model) {

        orderService.cancelOrder(orderId);
        model.addAttribute("account", account);

        return "redirect:/account/orderList";
    }

    @GetMapping("/account/orderCancelList")
    public String accountOrderCancelList(@CurrentAccount Account account,
                                   @PageableDefault(size = 9,direction = Sort.Direction.DESC)Pageable pageable,Model model) {

        Page<Order> orders = orderService.findByAccountCancelOrder(pageable, account);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "account/orderCancelList";
    }


    @GetMapping("/account/orderCompleteList")
    public String accountCompleteList(@CurrentAccount Account account,
                                      @PageableDefault(size = 9,direction = Sort.Direction.DESC)Pageable pageable,Model model){

        Page<Order> orders = orderService.findByAccountCompleteDelivery(pageable, account);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "account/orderCompleteList";
    }

    @GetMapping("/account/orderDeliveryList")
    public String accountDeliveryList(@CurrentAccount Account account,
                                      @PageableDefault(size = 9,direction = Sort.Direction.DESC)Pageable pageable,Model model){

        Page<Order> orders = orderService.findByAccountDeliveryList(pageable, account);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);

        return "account/orderDeliveryList";
    }


    /////////////////
    @PostMapping("/account/orders/{orderId}/deliveryComplete")
    public String accountDeliveryComplete(@CurrentAccount Account account,
                                           @PathVariable("orderId") Long orderId){

        if (account != null) {
            orderService.completeDelivery(orderId);
            return "redirect:/account/orderCompleteList";
        }

        return "index";

    }
}
