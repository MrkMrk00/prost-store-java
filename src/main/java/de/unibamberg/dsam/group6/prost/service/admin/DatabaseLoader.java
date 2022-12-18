package de.unibamberg.dsam.group6.prost.service.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.util.annotation.AdminAction;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
@AdminAction
public class DatabaseLoader {
    private final UserRepository userRepository;
    private final BottlesRepository bottlesRepository;
    private final PasswordEncoder encoder;

    private Map<String, Object> data = null;

    private Map<String, Object> getData() throws IOException {
        if (this.data == null) {
            var file = ResourceUtils.getFile("classpath:data.json");
            this.data = new ObjectMapper().readValue(file, HashMap.class);
        }
        return this.data;
    }

    @Async
    public Future<String> action__importAll() throws ExecutionException, InterruptedException, IOException {
        var users = this.action__importUsers();
        var bottles = this.action__importBottles();
        return new AsyncResult<>(users.get() + "\n" + bottles.get());
    }

    @Async
    public Future<String> action__importUsers() throws IOException {
        var users = (List<Map<String, String>>) this.getData().get("users");
        users.forEach(u -> {
            var b = u.get("birthday").split("-");

            this.userRepository.save(User.builder()
                    .username(u.get("username"))
                    .password(this.encoder.encode(u.get("password")))
                    .birthday(LocalDate.of(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])))
                    .build());
        });

        return new AsyncResult<>("Users imported successfully.");
    }

    @Async
    public Future<String> action__importBottles() throws IOException {
        var bottles = (List<Map<String, Object>>) this.getData().get("bottles");

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

        return new AsyncResult<>("Bottles imported successfully.");
    }
}
