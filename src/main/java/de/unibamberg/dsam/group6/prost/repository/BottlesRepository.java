package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Bottle;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottlesRepository extends JpaRepository<Bottle, Long> {

    @Override
    Optional<Bottle> findById(@NotNull Long id);

    @Override
    boolean existsById(@NotNull Long id);

    @Override
    Page<Bottle> findAll(Pageable pageable);
}
