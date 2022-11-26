package de.unibamberg.dsam.group6.prost.entity;

import java.util.Objects;
import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

@Entity(name = "bottles")
@Getter
@Setter
@NoArgsConstructor
public class Bottle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    @NotEmpty
    @Pattern(regexp = "\\w+")
    private String name;

    @Column(name = "bottle_pic")
    @URL
    private String bottlePic;

    @Column(name = "volume")
    @Min(1)
    private double volume;

    @Column(name = "is_alcoholic")
    @Setter(AccessLevel.NONE)
    private boolean isAlcoholic;

    @Column(name = "volume_percent")
    @Setter(AccessLevel.NONE)
    @Min(0)
    private double volumePercent;

    @Column(name = "price")
    @Min(1)
    private int price;

    @Column(name = "supplier")
    @NotEmpty
    private String supplier;

    @Column(name = "in_stock")
    @Min(0)
    private int inStock;

    // region Relation
    @ManyToOne(targetEntity = Crate.class, optional = false)
    @JoinColumn(name = "crate_id")
    private Crate crate;

    // endregion

    public void setVolumePercent(double volumePercent) {
        if (volumePercent < 0) {
            throw new ValidationException();
        }
        this.isAlcoholic = volumePercent > 0;
        this.volumePercent = volumePercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bottle bottle = (Bottle) o;
        return id != null && Objects.equals(id, bottle.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
