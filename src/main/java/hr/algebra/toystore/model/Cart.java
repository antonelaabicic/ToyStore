package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "CART")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id", unique = true)
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartItem> items = new ArrayList<>();

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPriceOfItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}