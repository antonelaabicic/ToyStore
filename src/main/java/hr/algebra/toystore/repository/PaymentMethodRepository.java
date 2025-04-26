package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
