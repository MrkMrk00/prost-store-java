package de.unibamberg.dsam.group6.prost.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Min;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "orders")
@NamedEntityGraph(
        name = "order-beverages",
        attributeNodes = @NamedAttributeNode(value = "orderItems", subgraph = "orderItem.beverage"),
        subgraphs =
                @NamedSubgraph(
                        name = "orderItem.beverage",
                        attributeNodes = {@NamedAttributeNode("beverage")}))
@NamedEntityGraph(
        name = "order-usernames",
        attributeNodes = @NamedAttributeNode(value = "user", subgraph = "user.username"),
        subgraphs = @NamedSubgraph(name = "user.username", attributeNodes = @NamedAttributeNode("username")))
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "price")
    @Min(1)
    private double price;

    @Column(name = "created_on", updatable = false, nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    // region Relations

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
