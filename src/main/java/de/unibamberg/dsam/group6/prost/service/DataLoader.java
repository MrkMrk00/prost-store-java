package de.unibamberg.dsam.group6.prost.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var data = new ObjectMapper().readValue(ResourceUtils.getFile("classpath:data.json"), HashMap.class);
        var users = (List<Map<String, String>>) data.get("users");

        users.forEach(u -> {
            var b = u.get("birthday").split("-");

            this.userRepository.save(User.builder()
                    .username(u.get("username"))
                    .password(this.encoder.encode(u.get("password")))
                    .birthday(LocalDate.of(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])))
                    .build());
        });
    }
}
