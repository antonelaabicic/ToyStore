package hr.algebra.toystore.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToyDto {

    private Integer id;

    @Size(min = 3, max = 50, message = "The name must contain between {min} and {max} characters.")
    private String name;

    @Size(min = 5, message = "Description should be at least 5 characters long.")
    private String description;

    @NotNull(message = "The price cannot be empty.")
    @DecimalMin(value = "0.0", message = "The price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "Stock is required.")
    @Min(value = 0, message = "Stock cannot be negative.")
    private Integer stock;

    @NotEmpty(message = "Category name is required.")
    private String categoryString;

    @NotEmpty(message = "Image URL is required.")
    private String imageUrl;

    @Transient
    private MultipartFile image;

    public ToyDto(Integer id, String name, String description, BigDecimal price, Integer stock, String categoryString, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryString = categoryString;
        this.imageUrl = imageUrl;
    }
}
