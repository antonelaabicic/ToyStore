package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CART_ITEM")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "toy_id")
    private Toy toy;

    @Column(name = "quantity")
    private Integer quantity;

    public CartItem(Toy toy, Integer quantity) {
        this.toy = toy;
        this.quantity = quantity;
    }

    public java.math.BigDecimal getTotalPriceOfItem() {
        return toy.getPrice().multiply(java.math.BigDecimal.valueOf(quantity));
    }
}