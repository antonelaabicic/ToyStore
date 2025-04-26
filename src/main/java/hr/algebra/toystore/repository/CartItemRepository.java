package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.Cart;
import hr.algebra.toystore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);
}
