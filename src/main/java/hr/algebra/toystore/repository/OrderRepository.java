package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(ApplicationUser user);
}
