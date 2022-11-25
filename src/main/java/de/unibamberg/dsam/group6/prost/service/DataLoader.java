package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository repo;

    @Autowired
    public DataLoader(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userlist = List.of(
                new User("exampleUser",
                        "asdasd",
                        LocalDate.of(1969, 6, 9),
                        Collections.emptyList(),
                        null,
                        null
                )
        );
        this.repo.saveAll(userlist);
    }
}
