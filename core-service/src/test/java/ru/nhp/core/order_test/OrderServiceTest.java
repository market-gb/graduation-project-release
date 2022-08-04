package ru.nhp.core.order_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.api.dto.cart.CartItemDto;
import ru.nhp.api.dto.core.OrderDetailsDto;
import ru.nhp.api.enums.OrderStatus;
import ru.nhp.core.entities.Category;
import ru.nhp.core.entities.Order;
import ru.nhp.core.entities.OrderItem;
import ru.nhp.core.entities.Product;
import ru.nhp.core.integrations.CartServiceIntegration;
import ru.nhp.core.repositories.OrderRepository;
import ru.nhp.core.services.OrderService;
import ru.nhp.core.services.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest(classes = {OrderService.class})
public class OrderServiceTest {
    @Autowired
    private OrderService ordersService;
    @MockBean
    private OrderRepository ordersRepository;
    @MockBean
    private CartServiceIntegration cartServiceIntegration;
    @MockBean
    private ProductService productsService;
    private final static String USERNAME = "test_username";
    private final static String ADDRESS = "test_address";
    private final static String PHONE = "test_phone";
    private final static String CATEGORY_TITLE = "test_category_title";
    private final static String CATEGORY_DESCRIPTION = "Category_description";
    private final static String CATEGORY_PATHNAME = "Category_pathname";
    private final static String TITLE = "Milk";
    private static final BigDecimal PRICE_PER_PRODUCT = BigDecimal.valueOf(100);
    private final static BigDecimal PRICE = BigDecimal.valueOf(100);
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100);
    private final static Integer QUANTITY = 1;
    private static OrderDetailsDto orderDetailsDto;
    private static CartItemDto cartItemDto;
    private static CartDto cartDto;
    private static Product product;
    private static Order order;
    private static List<Order> orderList;

    @BeforeAll
    public static void initEntities() {
        orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setAddress(ADDRESS);
        orderDetailsDto.setPhone(PHONE);

        cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setProductTitle(TITLE);
        cartItemDto.setPrice(PRICE);
        cartItemDto.setQuantity(QUANTITY);
        cartItemDto.setPricePerProduct(PRICE_PER_PRODUCT);

        cartDto = new CartDto();
        cartDto.setTotalPrice(TOTAL_PRICE);
        cartDto.setItems(List.of(cartItemDto));

        product = new Product();
        product.setId(cartItemDto.getProductId());
        product.setTitle(cartItemDto.getProductTitle());
        product.setPrice(cartItemDto.getPricePerProduct());
        product.setCategories(Set.of(Category.builder()
                .id(1L)
                .title(CATEGORY_TITLE)
                .description(CATEGORY_DESCRIPTION)
                .pathname(CATEGORY_PATHNAME)
                .build()));

        order = new Order();
        order.setId(1L);
        order.setAddress(orderDetailsDto.getAddress());
        order.setPhone(orderDetailsDto.getPhone());
        order.setUsername(USERNAME);
        order.setTotalPrice(cartDto.getTotalPrice());
        order.setOrderStatus(OrderStatus.CREATED);
        Set<OrderItem> items = cartDto.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(i.getQuantity());
                    item.setPricePerProduct(i.getPricePerProduct());
                    item.setPrice(i.getPrice());
                    item.setProduct(product);
                    return item;
                }).collect(Collectors.toSet());
        order.setItems(items);
        orderList = List.of(order);
    }

    @Test
    public void saveTest() {
        Mockito.doReturn(cartDto).when(cartServiceIntegration).getUserCart(USERNAME);
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(cartItemDto.getProductId());
        Order newOrder = ordersService.save(USERNAME, orderDetailsDto);
        Assertions.assertEquals(OrderStatus.CREATED, newOrder.getOrderStatus());
        Assertions.assertEquals(cartDto.getTotalPrice(), newOrder.getTotalPrice());
        Assertions.assertEquals(cartDto.getItems().size(), newOrder.getItems().size());
        Mockito.verify(cartServiceIntegration, Mockito.times(1)).clearUserCart(USERNAME);
        Mockito.verify(cartServiceIntegration, Mockito.times(1)).getUserCart(USERNAME);
        Mockito.verify(ordersRepository, Mockito.times(1)).save(newOrder);
    }

    @Test
    public void findAllByUsernameTest() {
        Mockito.doReturn(orderList).when(ordersRepository).findAllByUsername(USERNAME);
        List<Order> orders = ordersService.findAllByUsername(USERNAME);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(USERNAME, orders.get(0).getUsername());
        Assertions.assertEquals(OrderStatus.CREATED, orders.get(0).getOrderStatus());
        Assertions.assertEquals(ADDRESS, orders.get(0).getAddress());
        Assertions.assertEquals(PHONE, orders.get(0).getPhone());
        Assertions.assertEquals(TOTAL_PRICE, orders.get(0).getTotalPrice());
        Mockito.verify(ordersRepository, Mockito.times(1)).findAllByUsername(USERNAME);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(order)).when(ordersRepository).findById(1L);
        Order currentOrder = ordersService.findById(1L).orElse(new Order());
        Assertions.assertEquals(1, currentOrder.getId());
        Assertions.assertEquals(OrderStatus.CREATED, currentOrder.getOrderStatus());
        Assertions.assertEquals(USERNAME, currentOrder.getUsername());
        Assertions.assertEquals(ADDRESS, currentOrder.getAddress());
        Assertions.assertEquals(PHONE, currentOrder.getPhone());
        Assertions.assertEquals(order.getItems(), currentOrder.getItems());
        Assertions.assertEquals(TOTAL_PRICE, currentOrder.getTotalPrice());
        Mockito.verify(ordersRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteByIdTest() {
        ordersService.deleteById(1L);
        Mockito.verify(ordersRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void changeStatusTest() {
        ordersService.changeStatus(OrderStatus.PAID, 1L);
        Mockito.verify(ordersRepository, Mockito.times(1)).changeStatus(OrderStatus.PAID, 1L);
    }
}
