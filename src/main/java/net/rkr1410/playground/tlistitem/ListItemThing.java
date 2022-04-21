package net.rkr1410.playground.tlistitem;

import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.tlist.ListThing;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Collections;
import java.util.Set;

@Entity
public class ListItemThing extends Thing {

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private ListThing parent;

    public ListItemThing() {
    }

    public ListItemThing(String itemText) {
        setShortDesc(itemText);
    }

    public String getItemContents() {
        return getShortDesc();
    }

    public ListThing getParent() {
        return parent;
    }

    public void setParent(ListThing parent) {
        this.parent = parent;
    }

    @Override public Set<? extends Thing> getChildren() {
        return Collections.emptySet();
    }
}
