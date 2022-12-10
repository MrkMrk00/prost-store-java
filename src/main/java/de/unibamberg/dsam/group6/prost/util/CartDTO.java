package de.unibamberg.dsam.group6.prost.util;

import de.unibamberg.dsam.group6.prost.entity.Beverage;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.Crate;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CartDTO {
    public final Map<Bottle, Integer> bottles = new HashMap<>();
    public final Map<Crate, Integer> crates = new HashMap<>();

    private double totalPrice = -1;

    public double getTotalPrice() {
        if (this.totalPrice == -1) {
            this.recalculatePrice();
        }
        return this.totalPrice;
    }

    public void recalculatePrice() {
        var orderItems = new HashMap<Beverage, Integer>();
        orderItems.putAll(this.bottles);
        orderItems.putAll(this.crates);

        this.totalPrice = orderItems
                .keySet()
                .stream()
                .reduce(0.0, (prev, cur) -> prev + orderItems.get(cur) * cur.getPrice(), Double::sum);
    }
}
