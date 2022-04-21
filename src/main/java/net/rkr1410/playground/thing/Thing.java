package net.rkr1410.playground.thing;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class Thing {
    // TODO Maybe a GUID after all? Could have an id-before-insert then,
    // and same id generation strategy both for SQL and document DBs

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // spring's jpa default is 255
    @Size(max = 255)
    private String shortDesc;

    public long getId() {
        return id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public abstract Set<? extends Thing> getChildren();

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
