package net.rkr1410.playground.thing;

import net.rkr1410.playground.tag.Tag;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Thing {
    // TODO Maybe a GUID after all? Could have an id-before-insert then,
    // and same id generation strategy both for SQL and document DBs

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ThingType type;

    @NotNull
    @Size(max = 255)
    @Column(name = "short_desc", nullable = false)
    private String shortDesc;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Thing parent;

    // TODO Use sets instead of lists as soon ad uid is in place, also in other entities
    //  ...or don't? as it can hide bag exception, n+1 or cartesian product queries...
    //  https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl
    @ManyToMany
    @JoinTable(name = "thing_tags",
            joinColumns = @JoinColumn(name = "thing_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    referencedColumnName = "id"))
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "parent")
    private List<Thing> children = new ArrayList<>();

    public Thing() {
    }

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
        child.setParent(this);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.addTaggedThing(this);
    }

    public Thing getParent() {
        return parent;
    }

    public void setParent(Thing parent) {
        this.parent = parent;
    }

    public List<? extends Thing> getChildren() {
        return children;
    }

    public List<Tag> getTags() {
        return tags;
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
