package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TOY_CATEGORY")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class ToyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column (name = "NAME")
    private String name;

    public ToyCategory(String name) {
        this.name = name;
    }
}