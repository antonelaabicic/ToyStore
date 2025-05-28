package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.OrderDto;
import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.Order;

public class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("OrderMapper is a utility class.");
    }

    public static OrderDto toDto(Order entity) {

        OrderDto orderDto = new OrderDto(
                entity.getId(),
                entity.getOrderDate(),
                PaymentMethodMapper.toDto(entity.getPaymentMethod()),
                CartMapper.toDto(entity.getCart()),
                new UserDto(
                        entity.getUser().getUsername(),
                        entity.getUser().getName(),
                        entity.getUser().getSurname(),
                        entity.getUser().getEmail(),
                        entity.getUser().getCountry(),
                        entity.getUser().getCity(),
                        entity.getUser().getAddress()
                )
        );
        System.out.println(entity.getCart());
        return orderDto;
    }
}
