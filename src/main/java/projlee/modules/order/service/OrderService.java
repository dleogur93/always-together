package projlee.modules.order.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.AccountRepository;
import projlee.modules.account.domain.Account;
import projlee.modules.cart.Cart;
import projlee.modules.cart.CartRepository;
import projlee.modules.cart.CartService;
import projlee.modules.order.domain.Delivery;
import projlee.modules.order.domain.DeliveryStatus;
import projlee.modules.order.domain.Order;
import projlee.modules.order.domain.OrderItem;
import projlee.modules.item.domain.Item;
import projlee.modules.item.repository.ItemRepository;
import projlee.modules.order.event.OrderReadyEvent;
import projlee.modules.order.form.OrderSearch;
import projlee.modules.order.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final AccountRepository accountRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final CartService cartService;

    private final CartRepository cartRepository;

    /**
     * 주문
     */
    public void order(Long accountId, Long itemId, int count) {

        Account account = accountRepository.findById(accountId).orElseThrow(()-> new IllegalArgumentException("Invalid account id"));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new IllegalArgumentException("Invalid item id"));

        Delivery delivery = Delivery.builder()
                .address(account.getAddress())
                .build();

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(),count);


        Order order = Order.createOrder(account, delivery, orderItem);


        orderRepository.save(order);

    }

    /**
     * 카트 전체 주문
     */

    public void orderFromCart(Account account) {
        Cart cart = cartService.getCart(account);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }


        Delivery delivery = Delivery.builder()
                .address(account.getAddress())
                .build();



        Order order = Order.createOrder(account, delivery,
                cart.getCartItems().stream()
                        .map(cartItem -> OrderItem.createOrderItem(cartItem.getItem(), cartItem.getItem().getPrice(), cartItem.getCount()))
                        .toArray(OrderItem[]::new));

        orderRepository.save(order);
        cartService.clearCart(account); // 주문 후 장바구니 비우기

    }

    //        List<OrderItem> orderItems = cart.getCartItems().stream()
//                .map(cartItem -> OrderItem.createOrderItem(cartItem.getItem(), cartItem.getItem().getPrice(), cartItem.getCount()))
//                .collect(Collectors.toList());
//
//        Order order = Order.createOrder(account, delivery, orderItems.toArray(new OrderItem[0]));

    /**
     * 주문 취소
     */

    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        order.cancelOrder();
    }

    public Page<Order> findAllWithItems(Pageable pageable) {
       return orderRepository.findAllWithItems(pageable);
    }

    public Page<Order> findSearchOrder(Pageable pageable, OrderSearch orderSearch) {

        return orderRepository.findSearchOrder(pageable, orderSearch);
    }

    public Page<Order> findByAccount(Pageable pageable, Account account) {
             return orderRepository.findByAccount(pageable,account);
    }

    public Page<Order> findByAccountCancelOrder(Pageable pageable, Account account) {
        return orderRepository.accountCancelOrder(pageable,account);
    }

    public Page<Order> findByAccountCompleteDelivery(Pageable pageable, Account account) {
        return orderRepository.accountCompleteDelivery(pageable,account);
    }

    public void readyOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        order.getDelivery().deliveryStatusReady();

        eventPublisher.publishEvent(new OrderReadyEvent(order));

    }

    public void completeDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
        order.getDelivery().deliveryStatusComp();
    }

    public Page<Order> findByOrderDelivery(Pageable pageable) {
        return orderRepository.OrderDeliveryList(pageable);
    }

    public Page<Order> findByOrderCancelList(Pageable pageable) {
        return orderRepository.OrderCancelList(pageable);
    }

    public Page<Order> findByOrderCompleteList(Pageable pageable) {
        return orderRepository.OrderCompleteList(pageable);
    }

    public Page<Order> findByAccountDeliveryList(Pageable pageable, Account account) {
        return orderRepository.orderDeliveryList(pageable,account);
    }

//    //////
//    public Order createOrderFromCart(Account account, Delivery delivery) {
//        Cart cart = cartRepository.findByAccount(account)
//                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
//
//        Order order = Order.createOrder(account, delivery,
//                cart.getCartItems().stream()
//                        .map(cartItem -> OrderItem.createOrderItem(cartItem.getItem(), cartItem.getItem().getPrice(), cartItem.getCount()))
//                        .toArray(OrderItem[]::new));
//
//        // 장바구니 비우기
//        cart.getCartItems().clear();
//        cartRepository.save(cart);
//
//        return orderRepository.save(order);
//    }



}

// ILUBCOFFEE12      LOVE12COFFEE