package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(value = "user-with-roles")
    Optional<User> findUserByUsername(String username);
}
