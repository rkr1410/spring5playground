package net.rkr1410.playground.tag;

import net.rkr1410.playground.thing.Thing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToMany(mappedBy = "tags")
    private List<Thing> taggedThings = new ArrayList<>();

    public Tag() {
    }

    public Tag(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void addTaggedThing(Thing thing) {
        taggedThings.add(thing);
    }

    public String getValue() {
        return value;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return id == tag.id;
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}
