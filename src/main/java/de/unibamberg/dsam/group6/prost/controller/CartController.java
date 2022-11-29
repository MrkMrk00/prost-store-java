package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final UserErrorManager errors;
    private final BottlesRepository bottlesRepository;

    @PostMapping("/cart")
    public String addToCart(@RequestParam Long bottleId, @RequestParam String next) {
        var bottle = this.bottlesRepository.findById(bottleId);
        if (bottle.isEmpty()) {
            errors.addToast(Toast.error("Si kkt"));
        } else {
            errors.addToast(
                    Toast.success("Successfully added bottle " + bottle.get().getName()));
        }
        return "redirect:" + (next == null ? "/" : next);
    }
}
