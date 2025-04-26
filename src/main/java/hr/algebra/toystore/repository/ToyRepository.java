package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.Toy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToyRepository extends JpaRepository<Toy, Integer> {
    List<Toy> findByCategory_Id(Integer categoryId);
}
