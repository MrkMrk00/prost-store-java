package de.unibamberg.dsam.group6.prost.util;

import de.unibamberg.dsam.group6.prost.entity.Bottle;
import de.unibamberg.dsam.group6.prost.entity.Crate;

import java.util.HashMap;
import java.util.Map;

public class CartDTO {
    public final Map<Bottle, Integer> bottles = new HashMap<>();
    public final Map<Crate, Integer> crates = new HashMap<>();


}
