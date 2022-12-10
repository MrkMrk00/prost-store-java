package de.unibamberg.dsam.group6.prost.entity;

import javax.persistence.*;
import lombok.*;

import java.util.List;

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

    public abstract String getName();
    public abstract double getPrice();
    public abstract String getPicture();
    public abstract double getInStock();
}
