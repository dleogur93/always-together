package projlee.modules.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.domain.Account;
import projlee.modules.item.domain.Item;
import projlee.modules.item.repository.ItemRepository;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    public void addCart(Account account, Long itemId, int count) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));
        Cart cart = getCreateCart(account);
        CartItem cartItem = CartItem.createCartItem(item, count);

        cart.addCartItem(cartItem);

        cartRepository.save(cart);
    }

    public void removeItemFromCart(Account account, Long cartItemId) {

        Cart cart = getCart(account);

        if (cart == null) {
            throw new IllegalArgumentException("카트가 비어있습니다.");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(ci -> ci.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid cart item ID"));

        cart.removeCartItem(cartItem);

        cartRepository.save(cart);
    }


    private Cart getCreateCart(Account account) {

        return cartRepository.findByAccount(account)
                .orElseGet(() -> cartRepository.save(Cart.builder().account(account).build()));

    }

    public Cart getCart(Account account) {
        return cartRepository.findByAccount(account).orElse(null);
    }

    public int calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToInt(cartItem -> cartItem.getItem().getPrice() * cartItem.getCount())
                .sum();
    }

    public void clearCart(Account account) {
        Cart cart = getCart(account);
        if (cart != null) {
            cartRepository.deleteById(cart.getId());
        }
    }
}
