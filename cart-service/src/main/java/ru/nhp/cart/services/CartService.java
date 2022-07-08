    package ru.nhp.cart.services;

    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import ru.nhp.api.exceptions.ResourceNotFoundException;
    import ru.nhp.cart.dto.ProductDto;
    import ru.nhp.cart.entities.Cart;
    import ru.nhp.cart.integrations.ProductsServiceIntegration;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.UUID;
    import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductsServiceIntegration productsServiceIntegration;
    private final Map<String, Object> redisTemplate = new HashMap<>();

    @Value("${utils.cart.prefix}")
    private String cartPrefix;

    public String getCartUuidFromSuffix(String suffix) {
        return cartPrefix + suffix;
    }

    public String generateCartUuid() {
        return UUID.randomUUID().toString();
    }

    public Cart getCurrentCart(String cartKey) {
        if (!redisTemplate.containsKey(cartKey)) {
            redisTemplate.put(cartKey, new Cart());
        }
        return (Cart) redisTemplate.get(cartKey);
    }

    public void addToCart(String cartKey, Long productId) {
        ProductDto productDto = productsServiceIntegration.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Невозможно добавить продукт в корзину. Продукт не найдет, id: " + productId));
        execute(cartKey, c -> {
            c.add(productDto);
        });
    }

    public void clearCart(String cartKey) {
        execute(cartKey, Cart::clear);
    }

    public void removeItemFromCart(String cartKey, Long productId) {
        execute(cartKey, c -> c.remove(productId));
    }

    public void decrementItem(String cartKey, Long productId) {
        execute(cartKey, c -> c.decrement(productId));
    }

    public void merge(String userCartKey, String guestCartKey) {
        Cart guestCart = getCurrentCart(guestCartKey);
        Cart userCart = getCurrentCart(userCartKey);
        userCart.merge(guestCart);
        updateCart(guestCartKey, guestCart);
        updateCart(userCartKey, userCart);
    }

    private void execute(String cartKey, Consumer<Cart> action) {
        Cart cart = getCurrentCart(cartKey);
        action.accept(cart);
        redisTemplate.put(cartKey, cart);
    }

    public void updateCart(String cartKey, Cart cart) {
        redisTemplate.put(cartKey, cart);
    }
}