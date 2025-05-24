package hr.algebra.toystore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Integer id;
    private Integer toyId;
    private String toyName;
    private BigDecimal toyPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
