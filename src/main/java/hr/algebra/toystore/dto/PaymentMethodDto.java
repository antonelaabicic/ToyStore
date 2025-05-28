package hr.algebra.toystore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private Integer id;
    private String name;
}
