package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.entity.Order;
import de.unibamberg.dsam.group6.prost.repository.BeveragesRepository;
import de.unibamberg.dsam.group6.prost.repository.OrdersRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.Cart;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import de.unibamberg.dsam.group6.prost.util.exception.BadRequestException;
import java.security.Principal;
import java.util.Optional;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final BeveragesRepository beveragesRepo;
    private final UserRepository userRepo;
    private final OrdersRepository ordersRepo;
    private final Validator validator;
    private final UserErrorManager errors;

    @GetMapping("/cart")
    public String showCart(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("activeUser", auth.getPrincipal().toString());
        model.addAttribute("cart", this.cart.getCartState());
        return "pages/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Optional<Long> beverageId,
            @RequestParam Optional<Integer> count,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (beverageId.isEmpty()) {
            throw new BadRequestException();
        }
        var beverage = this.beveragesRepo.findById(beverageId.get());

        if (beverage.isPresent()) {
            this.cart.addToCart(beverageId.get(), count.orElse(1));
        }
        return "redirect:" + next.orElse("/");
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Optional<Long> beverageId,
            @RequestParam Optional<Boolean> all,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (beverageId.isEmpty()) {
            throw new BadRequestException();
        }
        if (all.orElse(false)) {
            this.cart.removeAllFromCart(beverageId.get());
        } else {
            this.cart.removeOneFromCart(beverageId.get());
        }
        return "redirect:" + next.orElse("/");
    }

    @PostMapping("/cart/submit")
    @Transactional
    public String submitCart(Principal principal) {
        var user = this.userRepo.findUserByUsername(principal.getName()).orElseThrow();
        var cartState = this.cart.getCartState();

        var order = new Order();
        order.setUser(user);
        order.setOrderItems(cartState.getOrderItems());
        order.setPrice(cartState.getTotalPrice());

        var res = this.validator.validate(order);
        if (!res.isEmpty()) {
            res.forEach(v -> this.errors.addToast(Toast.error(v.getMessage())));
            return "redirect:/cart";
        }

        this.ordersRepo.save(order);

        // reduce pieces in stock
        var reducedPiecesBeverages = cartState.beverages.entrySet().stream().map(entry -> {
            var bev = entry.getKey();
            var reduced = bev.getInStock() - entry.getValue();

            bev.setInStock(reduced < 0 ? 0 : reduced);
            return bev;
        }).toList();

        this.beveragesRepo.saveAll(reducedPiecesBeverages);

        this.cart.clear();
        this.errors.addToast(Toast.success("Order created successfully. :)"));
        return "redirect:/orders";
    }
}
