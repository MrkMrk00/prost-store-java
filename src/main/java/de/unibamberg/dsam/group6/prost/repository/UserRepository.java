package de.unibamberg.dsam.group6.prost.repository;

import de.unibamberg.dsam.group6.prost.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {}
