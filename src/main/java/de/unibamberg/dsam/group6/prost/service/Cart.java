package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.entity.Order;
import de.unibamberg.dsam.group6.prost.entity.OrderItem;
import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.CratesRepository;
import de.unibamberg.dsam.group6.prost.util.CartDTO;
import de.unibamberg.dsam.group6.prost.util.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        var initialCart = this.getCartItemIds();
        var bottleOpt = this.bottlesRepository.findById(beverageId);
        var crateOpt = this.cratesRepository.findById(beverageId);

        if (bottleOpt.isPresent()) {
            var bottle = bottleOpt.get();

            if (count > bottle.getInStock()) {
                this.errors.addToast(Toast.error("There isn't enough bottles left in stock!"));
                return;
            }

            initialCart.put(beverageId, initialCart.getOrDefault(beverageId, 0) + count);
            this.setCartItems(initialCart);
            this.errors.addToast(Toast.success("%d %s %s added to cart!", count, (count > 1 ? "bottles": "bottle"), bottle.getName()));

        } else if (crateOpt.isPresent()) {
            var crate = crateOpt.get();

            if (count > crate.getCratesInStock()) {
                this.errors.addToast(Toast.error("There isn't enough bottles in stock!"));
                return;
            }

            initialCart.put(beverageId, initialCart.getOrDefault(beverageId, 0) + count);
            this.setCartItems(initialCart);
            this.errors.addToast(Toast.success("%d %s %s added to cart!", count, (count > 1 ? "crates": "crate"), crate.getName()));

        } else {
            this.errors.addToast(Toast.error("This beverage does not exist!"));
        }
    }

    public void removeOneFromCart(Long beverageId) {
        var initialCart = this.getCartItemIds();
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
        this.errors.addToast(Toast.success("Successfully removed from cart."));
    }

    public void removeAllFromCart(Long beverageId) {
        var initialCart = this.getCartItemIds();
        initialCart.remove(beverageId);
        this.setCartItems(initialCart);
        this.errors.addToast(Toast.success("Successfully removed from cart."));
    }

    public Map<Long, Integer> getCartItemIds() {
        var cart = this.session.getAttribute(CART_SESSION_KEY);
        if (!(cart instanceof Map)) {
            cart = new HashMap<Long, Integer>();
            this.session.setAttribute(CART_SESSION_KEY, cart);
        }
        return (Map<Long, Integer>) cart;
    }

    public CartDTO getCartState() {
        var itemsForDisplay = new CartDTO();
        var cartItems = this.getCartItemIds();

        cartItems.forEach((id, count) -> {
            var bottle = this.bottlesRepository.findById(id);
            if (bottle.isPresent()) {
                itemsForDisplay.bottles.put(bottle.get(), count);
            } else {
                var crate = this.cratesRepository.findById(id);
                crate.ifPresent(c -> itemsForDisplay.crates.put(c, count));
            }
        });

        return itemsForDisplay;
    }

    public List<OrderItem> getOrderItems(Order order, CartDTO cartState) {
        var orderItems = new ArrayList<OrderItem>();
        return orderItems;
    }

    public List<OrderItem> getOrderItems(Order order) {
        return this.getOrderItems(order, this.getCartState());
    }

    public void setCartItems(@NotNull Map<Long, Integer> cart) {
        this.session.setAttribute(CART_SESSION_KEY, cart);
    }
}
