package ru.nhp.core.order_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nhp.api.dto.core.OrderDetailsDto;
import ru.nhp.api.dto.core.OrderDto;
import ru.nhp.api.dto.core.OrderItemDto;
import ru.nhp.api.dto.core.enums.OrderStatus;
import ru.nhp.core.controllers.OrderController;
import ru.nhp.core.converters.OrderConverter;
import ru.nhp.core.entities.Category;
import ru.nhp.core.entities.Order;
import ru.nhp.core.entities.OrderItem;
import ru.nhp.core.entities.Product;
import ru.nhp.core.services.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderConverter orderConverter;
    private final static String TITLE = "Milk";
    private final static String CATEGORY_TITLE = "Category";
    private final static String CATEGORY_DESCRIPTION = "Category_description";
    private final static String CATEGORY_PATHNAME = "Category_pathname";
    private static final BigDecimal PRICE_PER_PRODUCT = BigDecimal.valueOf(100);
    private final static BigDecimal PRICE = BigDecimal.valueOf(100);
    private final static Integer QUANTITY = 1;
    private static final String USERNAME = "test_user";
    private static final String FULL_NAME = "test_user_name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "123456";
    public static OrderDetailsDto orderDetailsDto;
    private static OrderDto orderDto;
    private static Order order;
    private static List<Order> orderList;

    @BeforeAll
    public static void initEntities() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle(TITLE);
        product.setPrice(PRICE_PER_PRODUCT);
        product.setCategories(Set.of(Category.builder()
                .id(1L)
                .title(CATEGORY_TITLE)
                .description(CATEGORY_DESCRIPTION)
                .pathname(CATEGORY_PATHNAME)
                .build()));

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setProductTitle(TITLE);
        orderItemDto.setQuantity(QUANTITY);
        orderItemDto.setPricePerProduct(PRICE_PER_PRODUCT);
        orderItemDto.setPrice(PRICE);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUsername(USERNAME);
        orderDto.setAddress(ADDRESS);
        orderDto.setPhone(PHONE);
        orderDto.setOrderStatus(OrderStatus.CREATED);
        orderDto.setItems(Set.of(orderItemDto));

        order = new Order();
        order.setId(1L);
        order.setUsername(USERNAME);
        order.setTotalPrice(PRICE);
        order.setAddress(ADDRESS);
        order.setPhone(PHONE);
        order.setOrderStatus(OrderStatus.CREATED);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(QUANTITY);
        orderItem.setPrice(PRICE);
        orderItem.setPricePerProduct(PRICE_PER_PRODUCT);

        order.setItems(Set.of(orderItem));
        orderList = List.of(order);
        orderDetailsDto = new OrderDetailsDto(FULL_NAME, ADDRESS, PHONE);
    }

    @Test
    public void saveTest() throws Exception {
        given(orderService.save(USERNAME, orderDetailsDto)).willReturn(order);
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("username", USERNAME)
                        .content(objectMapper
                                .writeValueAsString(orderDetailsDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllByUsernameTest() throws Exception {
        given(orderService.findAllByUsername(USERNAME)).willReturn(orderList);
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(get("/api/v1/orders/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("username", USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(USERNAME)));
    }

    @Test
    public void getByIdTest() throws Exception {
        given(orderService.findById(1L)).willReturn(Optional.of(order));
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is(USERNAME)));
    }

    @Test
    public void changeStatusTest() throws Exception {
        mvc.perform(patch("/api/v1/orders/statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderStatus", OrderStatus.PAID.name()
                                ))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mvc.perform(delete("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
