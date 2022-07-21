package ru.nhp.cart;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.api.dto.cart.CartItemDto;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.cart.entities.Cart;
import ru.nhp.cart.entities.CartItem;
import ru.nhp.cart.integrations.ProductsServiceIntegration;
import ru.nhp.cart.services.CartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(classes = {CartService.class, TestRedisConfiguration.class})
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private ProductsServiceIntegration productsServiceIntegration;
    private final static String CATEGORY_TITLE = "test_category_title";
    private final static String TITLE = "Bread";
    private static final BigDecimal PRICE_PER_PRODUCT = BigDecimal.valueOf(50.00);
    private final static BigDecimal PRICE = BigDecimal.valueOf(50.00);
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(50.00);
    private final static Integer QUANTITY = 1;
    private static ProductDto productDto;

    @BeforeEach
    public void initCart() {
        cartService.clearCart("wtfTest");

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setProductTitle(TITLE);
        cartItemDto.setPrice(PRICE);
        cartItemDto.setQuantity(QUANTITY);
        cartItemDto.setPricePerProduct(PRICE_PER_PRODUCT);

        CartDto cartDto = new CartDto();
        cartDto.setTotalPrice(TOTAL_PRICE);
        cartDto.setItems(List.of(cartItemDto));

        productDto = new ProductDto();
        productDto.setId(cartItemDto.getProductId());
        productDto.setTitle(cartItemDto.getProductTitle());
        productDto.setPrice(cartItemDto.getPricePerProduct());
        productDto.setCategories(Set.of(new CategoryDto(1L, CATEGORY_TITLE)));
    }

    @Test
    public void addToCartTest() {
        Mockito.doReturn(Optional.of(productDto)).when(productsServiceIntegration).findById(1L);
        cartService.addToCart("wtfTest", 1L);
        cartService.addToCart("wtfTest", 1L);
        cartService.addToCart("wtfTest", 1L);
        Assertions.assertEquals(1, cartService.getCurrentCart("wtfTest").getItems().size());
    }

    @Test
    public void mergeTest() {
        CartItem cartItem0 = new CartItem(1L, "X", 15, BigDecimal.valueOf(200.00), BigDecimal.valueOf(3000.00));
        CartItem cartItem1 = new CartItem(2L, "X", 15, BigDecimal.valueOf(200.00), BigDecimal.valueOf(3000.00));
        CartItem cartItem2 = new CartItem(3L, "X", 15, BigDecimal.valueOf(200.00), BigDecimal.valueOf(3000.00));
        CartItem cartItem3 = new CartItem(4L, "X", 15, BigDecimal.valueOf(200.00), BigDecimal.valueOf(3000.00));

        List<CartItem> cartItemList0 = new ArrayList<>();
        cartItemList0.add(cartItem0);
        cartItemList0.add(cartItem1);

        Cart guestCart1 = new Cart();
        guestCart1.setItems(cartItemList0);
        cartService.clearCart("wtfGuestCartTest");
        cartService.updateCart("wtfGuestCartTest", guestCart1);

        List<CartItem> cartItemList1 = new ArrayList<>();
        cartItemList1.add(cartItem2);
        cartItemList1.add(cartItem3);

        Cart userCart1 = new Cart();
        userCart1.setItems(cartItemList1);
        cartService.clearCart("wtfUserCartTest");
        cartService.updateCart("wtfUserCartTest", userCart1);

        cartService.merge("wtfUserCartTest", "wtfGuestCartTest");

        Assertions.assertEquals(4, cartService.getCurrentCart("wtfUserCartTest").getItems().size());
        Assertions.assertEquals(BigDecimal.valueOf(12000.00), cartService.getCurrentCart("wtfUserCartTest").getTotalPrice());
    }

    @Test
    public void getCurrentCartTest() {
        cartService.clearCart("wtfCartTest");
        Mockito.doReturn(Optional.of(productDto)).when(productsServiceIntegration).findById(1L);
        cartService.addToCart("wtfCartTest", 1L);
        Assertions.assertEquals(BigDecimal.valueOf(50.00), cartService.getCurrentCart("wtfCartTest").getTotalPrice());
    }

    @Test
    public void removeItemFromCartTest() {
        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setTitle("X");
        productDto2.setPrice(BigDecimal.valueOf(200.00));
        ProductDto productDto3 = new ProductDto();
        productDto3.setId(3L);
        productDto3.setTitle("X2");
        productDto3.setPrice(BigDecimal.valueOf(200.00));

        Mockito.doReturn(Optional.of(productDto)).when(productsServiceIntegration).findById(1L);
        Mockito.doReturn(Optional.of(productDto2)).when(productsServiceIntegration).findById(2L);
        Mockito.doReturn(Optional.of(productDto3)).when(productsServiceIntegration).findById(3L);
        cartService.addToCart("wtfCartTest", 1L);
        cartService.addToCart("wtfCartTest", 1L);
        cartService.addToCart("wtfCartTest", 2L);
        cartService.addToCart("wtfCartTest", 3L);
        Assertions.assertEquals(3, cartService.getCurrentCart("wtfCartTest").getItems().size());
        cartService.removeItemFromCart("wtfCartTest", 1L);
        Assertions.assertEquals(2, cartService.getCurrentCart("wtfCartTest").getItems().size());
    }

    @Test
    public void decrementItemTest() {
        cartService.clearCart("wtfCartTest");
        Mockito.doReturn(Optional.of(productDto)).when(productsServiceIntegration).findById(1L);
        cartService.addToCart("wtfCartTest", 1L);
        cartService.addToCart("wtfCartTest", 1L);
        Assertions.assertEquals(BigDecimal.valueOf(100.00), cartService.getCurrentCart("wtfCartTest").getTotalPrice());
        cartService.decrementItem("wtfCartTest", 1L);
        Assertions.assertEquals(BigDecimal.valueOf(50.00), cartService.getCurrentCart("wtfCartTest").getTotalPrice());
    }

    @Test
    public void clearCartTest() {
        cartService.clearCart("wtfCartTest");
        Assertions.assertEquals(0, cartService.getCurrentCart("wtfCartTest").getItems().size());
    }
}
