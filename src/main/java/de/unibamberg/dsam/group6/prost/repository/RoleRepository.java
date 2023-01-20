package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.*;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    Optional<Role> findById(Long aLong);

    Role findByName(String name);
}
