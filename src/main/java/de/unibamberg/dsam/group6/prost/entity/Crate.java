package de.unibamberg.dsam.group6.prost.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity(name = "crate")
@Getter
@Setter
@NoArgsConstructor
public class Crate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Pattern(regexp = "\\w+")
    private String name;

    @Column(name = "crate_pic")
    @URL
    private String cratePic;

    @Column(name = "no_of_bottles")
    @Min(1)
    private int noOfBottles;

    @Column(name = "price")
    @Min(1)
    private double price;

    @Column(name = "crates_in_stock")
    @Min(0)
    private int cratesInStock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Crate crate = (Crate) o;
        return id != null && Objects.equals(id, crate.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
