package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_username(String username);
    Optional<Order> findByUser_usernameAndId(String username, Long id);
}
