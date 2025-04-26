package hr.algebra.toystore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Table(name = "TOY_CATEGORY")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
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