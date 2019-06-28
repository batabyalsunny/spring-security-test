package ml.bootcode.springsecuritytest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ml.bootcode.springsecuritytest.models.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	Privilege findByName(String name);
}
