package de.unibamberg.dsam.group6.prost.service.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.Crate;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.CratesRepository;
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
    private final CratesRepository cratesRepository;
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
        final var sb = new StringBuilder()
                .append(this.action__importUsers().get())
                .append(System.lineSeparator())
                .append(this.action__importBottles().get())
                .append(System.lineSeparator())
                .append(this.action__importCrates().get())
                .append(System.lineSeparator());

        return new AsyncResult<>(sb.toString());
    }

    @Async
    public Future<String> action__importUsers() throws IOException {
        final var users = (List<Map<String, String>>) this.getData().get("users");
        users.forEach(u -> {
            var b = u.get("birthday").split("-");

            this.userRepository.save(User.builder()
                    .username(u.get("username"))
                    .password(this.encoder.encode(u.get("password")))
                    .birthday(LocalDate.of(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])))
                    .build());
        });
        this.userRepository.flush();
        return new AsyncResult<>("Users imported successfully.");
    }

    @Async
    public Future<String> action__importBottles() throws IOException {
        final var bottles = (List<Map<String, Object>>) this.getData().get("bottles");
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
        this.bottlesRepository.flush();
        return new AsyncResult<>("Bottles imported successfully.");
    }

    @Async
    public Future<String> action__importCrates() throws IOException {
        final var crates = (List<Map<String, Object>>) this.getData().get("crates");
        final var bottles = this.bottlesRepository.findAllBeerLike();
        var iter = 0;

        for (var c : crates) {
            if (iter >= bottles.size()) {
                iter = 0;
                this.cratesRepository.flush();
            }
            this.cratesRepository.save(Crate.builder()
                    .name((String) c.get("name"))
                    .cratePic((String) c.get("cratePic"))
                    .noOfBottles((int) c.get("noOfBottles"))
                    .price((double) c.get("price"))
                    .cratesInStock((int) c.get("cratesInStock"))
                    .bottle(bottles.get(iter))
                    .build());
            iter++;
        }
        this.cratesRepository.flush();
        return new AsyncResult<>("Crates imported successfully");
    }

    @Async
    public Future<String> action__clearDatabase() {
        this.cratesRepository.deleteAll();
        this.userRepository.deleteAll();
        this.bottlesRepository.deleteAll();

        return new AsyncResult<>("Cleared successfully.");
    }
}
