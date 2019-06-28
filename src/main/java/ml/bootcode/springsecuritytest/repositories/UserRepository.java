package ml.bootcode.springsecuritytest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ml.bootcode.springsecuritytest.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
