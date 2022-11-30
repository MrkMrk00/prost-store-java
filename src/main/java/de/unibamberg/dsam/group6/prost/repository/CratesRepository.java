package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.Crate;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CratesRepository extends JpaRepository<Crate, Long> {

    @Override
    Optional<Crate> findById(@NotNull Long id);

    @Override
    boolean existsById(@NotNull Long id);
}
