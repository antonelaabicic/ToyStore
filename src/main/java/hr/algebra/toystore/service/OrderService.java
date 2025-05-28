package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.OrderDto;
import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.OrderSearchForm;
;
import java.util.List;

public interface OrderService {
    void placeOrder(String sessionId, PaymentMethodDto paymentMethodDto, UserDto userDto);
    List<OrderDto> getOrdersByUser(UserDto userDto);
    List<OrderDto> getAllOrders();
    CartDto getCartForCheckout(String sessionId);
    List<OrderDto> searchOrders(OrderSearchForm searchForm);
}
