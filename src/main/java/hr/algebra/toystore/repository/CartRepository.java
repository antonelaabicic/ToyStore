package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findBySessionId(String sessionId);
    //Optional<Cart> findByUser(ApplicationUser user);
}
