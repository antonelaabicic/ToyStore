package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.OrderDto;
import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.*;
import hr.algebra.toystore.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ApplicationUserService applicationUserService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("placeOrder -> creates order and clears cart")
    void placeOrderSuccess() {
        Cart cart = cart("ABC");
        cart.getItems().add(item(1, toy(1, "Teddy"), 2));

        UserDto userDto = userDto("nina");
        ApplicationUser user = user("nina");

        PaymentMethodDto payment = PaymentMethodDto.builder()
                .id(1)
                .name("CARD")
                .build();

        when(cartService.findBySessionId("ABC")).thenReturn(cart);
        when(applicationUserService.findUserByUsername("nina")).thenReturn(user);

        orderService.placeOrder("ABC", payment, userDto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());

        Order saved = captor.getValue();

        assertEquals(user, saved.getUser());
        assertEquals(1, saved.getCart().getItems().size());
        assertEquals("Teddy", saved.getCart().getItems().get(0).getToy().getName());
        verify(cartService).clearCart("ABC");
    }

    @Test
    @DisplayName("getOrdersByUser -> returns mapped orders")
    void getOrdersByUser() {
        ApplicationUser user = user("nina");

        when(applicationUserService.findUserByUsername("nina")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(List.of(order(user)));

        UserDto dto = userDto("nina");
        List<OrderDto> result = orderService.getOrdersByUser(dto);

        assertEquals(1, result.size());
        verify(orderRepository).findByUser(user);
    }

    @Test
    @DisplayName("getAllOrders -> returns all")
    void getAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order(user("admin"))));

        List<OrderDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("getCartForCheckout -> delegates to cart service")
    void getCartForCheckout() {
        CartDto dto = new CartDto();
        dto.setSessionId("ABC");

        when(cartService.getCart("ABC")).thenReturn(dto);

        CartDto result = orderService.getCartForCheckout("ABC");

        assertEquals("ABC", result.getSessionId());
        verify(cartService).getCart("ABC");
    }

    @Test
    @DisplayName("searchOrders -> filters by username")
    void searchOrdersByUsername() {
        ApplicationUser user = user("nina");

        when(applicationUserService.findUserByUsername("nina")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(List.of(order(user)));

        OrderSearchForm form = new OrderSearchForm();
        form.setUsername("nina");

        List<OrderDto> result = orderService.searchOrders(form);

        assertEquals(1, result.size());
        verify(orderRepository).findByUser(user);
    }

    @Test
    @DisplayName("searchOrders -> filters by dates")
    void searchOrdersByDateRange() {
        ApplicationUser user = user("nina");

        Order inside = order(user);
        inside.setOrderDate(LocalDateTime.of(2025, Month.JUNE, 15, 12, 0));

        Order outside = order(user);
        outside.setOrderDate(LocalDateTime.of(2024, Month.JANUARY, 1, 12, 0));

        when(orderRepository.findAll()).thenReturn(List.of(inside, outside));

        OrderSearchForm form = new OrderSearchForm();
        form.setFromDate(LocalDate.of(2025, Month.JANUARY, 1));
        form.setToDate(LocalDate.of(2025, Month.DECEMBER, 31));

        List<OrderDto> result = orderService.searchOrders(form);

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("searchOrders -> no username uses findAll")
    void searchOrdersWithoutUsername() {
        when(orderRepository.findAll()).thenReturn(List.of(order(user("admin"))));

        OrderSearchForm form = new OrderSearchForm();
        List<OrderDto> result = orderService.searchOrders(form);

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    // HELPERS
    private ApplicationUser user(String username) {
        return ApplicationUser.builder()
                .username(username)
                .build();
    }

    private UserDto userDto(String username) {
        UserDto dto = new UserDto();
        dto.setUsername(username);
        return dto;
    }

    private Toy toy(Integer id, String name) {
        return Toy.builder()
                .id(id)
                .name(name)
                .build();
    }

    private CartItem item(Integer id, Toy toy, int quantity) {
        return CartItem.builder()
                .id(id)
                .toy(toy)
                .quantity(quantity)
                .build();
    }

    private Cart cart(String sessionId) {
        Cart cart = Cart.builder()
                .sessionId(sessionId)
                .build();

        cart.setItems(new ArrayList<>());

        return cart;
    }

    private Order order(ApplicationUser user) {
        return Order.builder()
                .user(user)
                .orderDate(LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0))
                .paymentMethod(paymentMethod())
                .cart(cart("ORDER"))
                .build();
    }

    private PaymentMethod paymentMethod() {
        return PaymentMethod.builder()
                .id(1)
                .name("CARD")
                .build();
    }
}