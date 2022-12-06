package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.CratesRepository;
import de.unibamberg.dsam.group6.prost.util.Toast;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Cart {
    private static final String CART_SESSION_KEY = "__cart";

    private final HttpSession session;
    private final UserErrorManager errors;
    private final BottlesRepository bottlesRepository;
    private final CratesRepository cratesRepository;

    // TODO: 16 / 18 years for alcoholic drinks
    public void addToCart(@NotNull Long beverageId, int count) {
        var initialCart = this.getCartItems();
        var bottleOpt = this.bottlesRepository.findById(beverageId);
        var crateOpt = this.cratesRepository.findById(beverageId);

        if (bottleOpt.isPresent()) {
            var bottle = bottleOpt.get();

            if (count > bottle.getInStock()) {
                this.errors.addToast(Toast.error("There isn't enough bottles in stock!"));
                return;
            }

            initialCart.put(beverageId, initialCart.getOrDefault(beverageId, 0) + count);
            this.setCartItems(initialCart);
            this.errors.addToast(Toast.success("Bottle %s added to cart!", bottle.getName()));

        } else if (crateOpt.isPresent()) {
            var crate = crateOpt.get();

            if (count > crate.getCratesInStock()) {
                this.errors.addToast(Toast.error("There isn't enough bottles in stock!"));
                return;
            }

            initialCart.put(beverageId, initialCart.getOrDefault(beverageId, 0) + count);
            this.setCartItems(initialCart);
            this.errors.addToast(Toast.success("Crate %s added to cart!", crate.getName()));

        } else {
            this.errors.addToast(Toast.error("This beverage does not exist!"));
        }
    }

    public void removeOneFromCart(Long beverageId) {
        var initialCart = this.getCartItems();
        var count = initialCart.getOrDefault(beverageId, -1);
        if (count == -1) {
            return;
        }

        initialCart.put(beverageId, count - 1);
        count = initialCart.get(beverageId);

        if (count <= 0) {
            initialCart.remove(beverageId);
        }

        this.setCartItems(initialCart);
    }

    public void removeAllFromCart(Long beverageId) {
        var initialCart = this.getCartItems();
        initialCart.remove(beverageId);
        this.setCartItems(initialCart);
    }

    public Map<Long, Integer> getCartItems() {
        var cart = this.session.getAttribute(CART_SESSION_KEY);
        if (!(cart instanceof Map)) {
            cart = new HashMap<Long, Integer>();
            this.session.setAttribute(CART_SESSION_KEY, cart);
        }
        return (Map<Long, Integer>) cart;
    }

    public void setCartItems(@NotNull Map<Long, Integer> cart) {
        this.session.setAttribute(CART_SESSION_KEY, cart);
    }
}
