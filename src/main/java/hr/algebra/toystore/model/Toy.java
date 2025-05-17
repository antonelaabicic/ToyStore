package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Builder
@Table(name = "TOY")
public class Toy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private ToyCategory category;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    public Toy(String name, String description, BigDecimal price, ToyCategory category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}
