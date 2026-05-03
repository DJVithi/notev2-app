package de.notev2.notev2.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import de.notev2.notev2.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
}
