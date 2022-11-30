package de.unibamberg.dsam.group6.prost.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
public class DatabaseLoader {
    private final UserRepository userRepository;
    private final BottlesRepository bottlesRepository;
    private final PasswordEncoder encoder;

    private Map<String, Object> data = null;

    private boolean tryCallback(Consumer<Map<String, Object>> callback) {
        try {
            callback.accept(this.getData());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Map<String, Object> getData() throws IOException {
        if (this.data == null) {
            var file = ResourceUtils.getFile("classpath:data.json");
            this.data = new ObjectMapper().readValue(file, HashMap.class);
        }
        return this.data;
    }

    @Async
    public Future<Boolean> importAll() {
        var users = this.importUsers();
        var bottles = this.importBottles();
        try {
            return new AsyncResult<>(users.get() && bottles.get());
        } catch (ExecutionException | InterruptedException e) {
            return new AsyncResult<>(false);
        }
    }

    @Async
    public Future<Boolean> importUsers() {
        var res = this.tryCallback(data -> {
            var users = (List<Map<String, String>>) data.get("users");
            users.forEach(u -> {
                var b = u.get("birthday").split("-");

                this.userRepository.save(User.builder()
                        .username(u.get("username"))
                        .password(this.encoder.encode(u.get("password")))
                        .birthday(LocalDate.of(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])))
                        .build());
            });
        });
        return new AsyncResult<>(res);
    }

    @Async
    public Future<Boolean> importBottles() {
        var res = this.tryCallback(data -> {
            var bottles = (List<Map<String, Object>>) data.get("bottles");

            bottles.forEach(b -> {
                this.bottlesRepository.save(Bottle.builder()
                        .name((String) b.get("name"))
                        .bottlePic((String) b.get("bottlePic"))
                        .volume((Double) b.get("volume"))
                        .volumePercent((Double) b.get("volumePercent"))
                        .price((Integer) b.get("price"))
                        .supplier((String) b.get("supplier"))
                        .inStock((Integer) b.get("inStock"))
                        .build());
            });
        });
        return new AsyncResult<>(res);
    }
}
