package hr.algebra.toystore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Integer id;
    private LocalDateTime orderDate;
    private PaymentMethodDto paymentMethod;
    private CartDto cart;
    private UserDto user;
}
