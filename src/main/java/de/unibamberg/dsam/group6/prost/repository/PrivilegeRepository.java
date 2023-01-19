package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Privilege;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Override
    Optional<Privilege> findById(Long aLong);

    Privilege findByName(String name);
}
