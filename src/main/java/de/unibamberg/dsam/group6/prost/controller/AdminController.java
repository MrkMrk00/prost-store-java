package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.service.DatabaseLoader;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserErrorManager errors;
    private final DatabaseLoader dbLoader;

    @Value("${spring.profiles.active}")
    private List<String> activeProfiles;

    @GetMapping("/admin/loadData")
    public String loadData(
            @RequestParam Optional<String> await, @RequestParam Optional<String> next, Authentication auth)
            throws ExecutionException, InterruptedException {
        if (!this.activeProfiles.contains("dev")
                && auth.getAuthorities().stream()
                        .noneMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            this.errors.addToast(Toast.error("You are not privileged to run this action."));
            return "redirect:" + (next.isPresent() ? next : "/");
        }

        var future = this.dbLoader.importAll();

        if (await.isEmpty()) {
            this.errors.addToast(Toast.info("Import started"));
            return "redirect:" + (next.isPresent() ? next : "/");
        }

        var res = future.get();
        if (res) {
            this.errors.addToast(Toast.success("Import successful :)"));
        } else {
            this.errors.addToast(Toast.error("Unable to import data :("));
        }

        return "redirect:" + (next.isPresent() ? next : "/");
    }
}
