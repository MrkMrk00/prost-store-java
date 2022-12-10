package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.entity.Order;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.Cart;
import de.unibamberg.dsam.group6.prost.util.exception.BadRequestException;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final BottlesRepository bottlesRepo;
    private final UserRepository userRepo;

    @GetMapping("/cart")
    public String showCart(Principal principal, Model model, @RequestParam Optional<Boolean> embedded) {
        var cartState = this.cart.getCartState();
        var user = this.userRepo.findUserByUsername(principal.getName()).orElseThrow();

        var order = new Order();
        order.setUser(user);
        order.setPrice(cartState.getTotalPrice());

        model.addAttribute("cart", cart.getCartState());
        model.addAttribute("order", order);

        if (embedded.orElse(false)) {
            return "components/cart";
        } else {
            return "pages/cart";
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Optional<Long> bottleId,
            @RequestParam Optional<Integer> count,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (bottleId.isEmpty()) {
            throw new BadRequestException();
        }
        var bottle = this.bottlesRepo.findById(bottleId.get());

        if (bottle.isPresent()) {
            this.cart.addToCart(bottleId.get(), count.orElse(1));
        }
        return "redirect:" + next.orElse("/");
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Optional<Long> bottleId,
            @RequestParam Optional<Boolean> all,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (bottleId.isEmpty()) {
            throw new BadRequestException();
        }
        if (all.orElse(false)) {
            this.cart.removeAllFromCart(bottleId.get());
        } else {
            this.cart.removeOneFromCart(bottleId.get());
        }
        return "redirect:" + next.orElse("/");
    }
}
