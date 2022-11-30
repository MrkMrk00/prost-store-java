package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.service.Cart;
import de.unibamberg.dsam.group6.prost.util.exception.BadRequestException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final BottlesRepository bottlesRepository;

    @GetMapping("/cart/list")
    @Profile("dev")
    @ResponseBody
    public String listCart() {
        return this.cart.getCartItems().toString();
    }

    @PostMapping("/cart")
    public String addToCart(
            @RequestParam Optional<Long> bottleId,
            @RequestParam Optional<Integer> count,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (bottleId.isEmpty()) {
            throw new BadRequestException();
        }
        var bottle = this.bottlesRepository.findById(bottleId.get());

        if (bottle.isPresent()) {
            this.cart.addToCart(bottleId.get(), count.orElse(1));
        }
        return "redirect:" + next.orElse("/");
    }
}
