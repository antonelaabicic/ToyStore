package hr.algebra.toystore.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToyCategoryDto {
    private Integer id;

    @Size(min = 3, max = 50, message = "The name must contain between {min} and {max} characters.")
    private String name;
}
