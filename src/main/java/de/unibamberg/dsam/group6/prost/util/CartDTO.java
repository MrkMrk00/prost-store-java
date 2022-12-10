package de.unibamberg.dsam.group6.prost.util;

import de.unibamberg.dsam.group6.prost.entity.Beverage;
import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.Crate;

import java.util.HashMap;
import java.util.Map;

public class CartDTO {
    public final Map<Bottle, Integer> bottles = new HashMap<>();
    public final Map<Crate, Integer> crates = new HashMap<>();

    public double getTotalPrice() {
        var orderItems = new HashMap<Beverage, Integer>();
        orderItems.putAll(this.bottles);
        orderItems.putAll(this.crates);

        return orderItems
                .keySet()
                .stream()
                .reduce(0.0, (prev, cur) -> prev + orderItems.get(cur) * cur.getPrice(), Double::sum);
    }
}
