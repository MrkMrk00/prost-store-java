package de.unibamberg.dsam.group6.prost.entity;

import java.util.List;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Beverage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "beverage")
    private List<OrderItem> orderItem;

    public boolean isBottle() {
        return this instanceof Bottle;
    }

    public boolean isCrate() {
        return this instanceof Crate;
    }

    public abstract String getName();

    public abstract double getPrice();

    public abstract String getPicture();

    public abstract double getInStock();
}
