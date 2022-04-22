package net.rkr1410.playground.thing;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Thing {
    // TODO Maybe a GUID after all? Could have an id-before-insert then,
    // and same id generation strategy both for SQL and document DBs

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ThingType type;

    // spring's jpa default is 255
    @Size(max = 255)
    private String shortDesc;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Thing parent;

    @OneToMany(mappedBy = "parent")
    private Set<Thing> children = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public ThingType getType() {
        return type;
    }

    public void setType(ThingType type) {
        this.type = type;
    }

    public void addChild(Thing child) {
        children.add(child);
        child.parent = this;
    }

    public Set<? extends Thing> getChildren() {
        return children;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thing)) return false;
        Thing thing = (Thing) o;
        return id == thing.id;
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}
