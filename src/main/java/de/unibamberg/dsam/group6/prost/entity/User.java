package de.unibamberg.dsam.group6.prost.entity;

import de.unibamberg.dsam.group6.prost.util.annotation.IsAfter;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @NotEmpty
    @Setter(AccessLevel.NONE)
    @Column(name = "username", nullable = false)
    private String username;

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "birthday", nullable = false)
    @Past
    @IsAfter(year = 1900)
    private LocalDate birthday;

    // region Relations

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return username != null && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
