package de.unibamberg.dsam.group6.prost.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibamberg.dsam.group6.prost.entity.Beverage;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
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
    private final BottlesRepository bottlesRepository;

    private final PasswordEncoder encoder;

    private void importUsers(Map<String, Object> data) {
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

    private void importBottles(Map<String, Object> data) {
        var bottles = (List<Map<String, Object>>) data.get("bottles");

        bottles.forEach(b -> {
            this.bottlesRepository.save(
                    Bottle.builder()
                            .name((String)b.get("name"))
                            .bottlePic((String)b.get("bottlePic"))
                            .volume((Double)b.get("volume"))
                            .volumePercent((Double)b.get("volumePercent"))
                            .price((Integer)b.get("price"))
                            .supplier((String)b.get("supplier"))
                            .inStock((Integer)b.get("inStock"))
                            .beverage(new Beverage())
                            .build()
            );
        });
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var data = new ObjectMapper().readValue(ResourceUtils.getFile("classpath:data.json"), Map.class);
        this.importUsers(data);
        this.importBottles(data);
    }
}
