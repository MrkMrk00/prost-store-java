package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.repository.BeveragesRepository;
import de.unibamberg.dsam.group6.prost.util.CartDTO;
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
    private final BeveragesRepository bevRepo;

    public void addToCart(@NotNull Long beverageId, int count) {
        var initialCart = this.getCartItemIds();
        var beverage = this.bevRepo.findById(beverageId);

        if (beverage.isEmpty()) {
            this.errors.addToast(Toast.error("Not found"));
            return;
        }

        initialCart.put(beverageId, initialCart.getOrDefault(beverageId, 0) + count);
        this.setCartItems(initialCart);
        this.errors.addToast(
                Toast.success("%d %s added to cart!", count, beverage.get().getName()));
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
            var bev = this.bevRepo.findById(id);
            bev.ifPresent(b -> itemsForDisplay.beverages.put(b, count));
        });

        return itemsForDisplay;
    }

    public void setCartItems(@NotNull Map<Long, Integer> cart) {
        this.session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart() {
        this.setCartItems(new HashMap<>());
    }
}
