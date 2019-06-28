package ml.bootcode.springsecuritytest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ml.bootcode.springsecuritytest.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
