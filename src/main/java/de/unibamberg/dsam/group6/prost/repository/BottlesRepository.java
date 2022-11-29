package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Bottle;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottlesRepository extends JpaRepository<Bottle, Long> {

    @Override
    Optional<Bottle> findById(Long aLong);
}
