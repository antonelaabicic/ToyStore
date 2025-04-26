package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    ApplicationUser findByUsername(String username);
}
