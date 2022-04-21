package net.rkr1410.playground.tlist;

import net.rkr1410.playground.tlistitem.ListItemThing;
import net.rkr1410.playground.thing.Thing;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ListThing extends Thing {

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private final Set<ListItemThing> listItems = new HashSet<>();

    public ListThing() {
    }

    public ListThing(String listName) {
        setShortDesc(listName);
    }

    public void addListItem(ListItemThing item) {
        listItems.add(item);
        // TODO ATM it's only the following line that saves the day when adding
        //  several ew objects in a row, as due to id-equality adding to set does
        //  nothing for new objects
        item.setParent(this);
    }

    public String getName() {
        return getShortDesc();
    }

    @Override public Set<ListItemThing> getChildren() {
        return listItems;
    }
}
