package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PAYMENT_METHOD")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column (name = "NAME")
    private String name;

    public PaymentMethod(String name) {
        this.name = name;
    }
}
