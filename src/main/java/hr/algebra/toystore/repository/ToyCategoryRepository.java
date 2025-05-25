package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.ToyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToyCategoryRepository extends JpaRepository<ToyCategory, Integer> {
    Optional<ToyCategory> findByName(String name);
}
