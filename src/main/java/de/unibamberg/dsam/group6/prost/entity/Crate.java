package de.unibamberg.dsam.group6.prost.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

@Entity(name = "crates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // region Relations

    @OneToOne(optional = false)
    private Beverage beverage;

    @OneToMany(mappedBy = "crate")
    private List<Bottle> bottles;

    // endregion

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
