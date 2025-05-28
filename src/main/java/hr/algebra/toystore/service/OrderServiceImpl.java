package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.OrderDto;
import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.*;
import hr.algebra.toystore.repository.CartRepository;
import hr.algebra.toystore.repository.OrderRepository;
import hr.algebra.toystore.repository.ToyRepository;
import hr.algebra.toystore.util.CartMapper;
import hr.algebra.toystore.util.OrderMapper;
import hr.algebra.toystore.util.PaymentMethodMapper;
import hr.algebra.toystore.util.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ApplicationUserService applicationUserService;

    @Override
    public void placeOrder(String sessionId, PaymentMethodDto paymentMethodDto, UserDto userDto) {
        Cart originalCart = cartService.findBySessionId(sessionId);

        Cart copiedCart = Cart.builder()
                .sessionId("ORDER_" + UUID.randomUUID())
                .build();

        List<CartItem> copiedItems = originalCart.getItems().stream()
                .map(item -> {
                    CartItem copy = new CartItem();
                    copy.setToy(item.getToy());
                    copy.setQuantity(item.getQuantity());
                    copy.setCart(copiedCart);
                    return copy;
                })
                .collect(Collectors.toList());

        copiedCart.setItems(copiedItems);

        ApplicationUser user = applicationUserService.findUserByUsername(userDto.getUsername());

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .paymentMethod(PaymentMethodMapper.toEntity(paymentMethodDto))
                .cart(copiedCart)
                .user(user)
                .build();

        orderRepository.save(order);

        cartService.clearCart(sessionId);
    }

    @Override
    public List<OrderDto> getOrdersByUser(UserDto userDto) {
        ApplicationUser user = applicationUserService.findUserByUsername(userDto.getUsername());

        List<OrderDto> orderDtos = orderRepository.findByUser(user).stream()
                .map(order -> OrderMapper.toDto(order))
                .collect(Collectors.toList());

        orderDtos.forEach(order -> System.out.println(order));

        return orderDtos;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartDto getCartForCheckout(String sessionId) {
        return cartService.getCart(sessionId);
    }

    @Override
    public List<OrderDto> searchOrders(OrderSearchForm searchForm) {
        List<Order> orders;

        if (searchForm.getUsername() != null && !searchForm.getUsername().isBlank()) {
            ApplicationUser user = applicationUserService.findUserByUsername(searchForm.getUsername());
            orders = orderRepository.findByUser(user);
        } else {
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .filter(order -> {
                    boolean matches = true;
                    if (searchForm.getFromDate() != null) {
                        matches &= !order.getOrderDate().toLocalDate().isBefore(searchForm.getFromDate());
                    }
                    if (searchForm.getToDate() != null) {
                        matches &= !order.getOrderDate().toLocalDate().isAfter(searchForm.getToDate());
                    }
                    return matches;
                })
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}
