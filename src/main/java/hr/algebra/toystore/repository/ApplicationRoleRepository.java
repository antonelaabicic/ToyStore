package hr.algebra.toystore.repository;

import hr.algebra.toystore.model.ApplicationRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRoleRepository extends JpaRepository<ApplicationRole, Long> {
    ApplicationRole findByName(String name);
}
