package net.rkr1410.playground;

import net.rkr1410.playground.tlist.ListThing;
import net.rkr1410.playground.tlist.ListThingRepository;
import net.rkr1410.playground.tlistitem.ListItemThing;
import net.rkr1410.playground.tlistitem.ListItemThingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BoostrapPlaygroundData implements CommandLineRunner {

    private final ApplicationContext ctx;
    private final ListThingRepository listRepository;
    private final ListItemThingRepository listItemRepository;

    public BoostrapPlaygroundData(ApplicationContext ctx,
                                  ListThingRepository listRepository, ListItemThingRepository listItemRepository) {
        this.ctx = ctx;
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
    }

    @Override public void run(String... args) throws Exception {
        ctx.getBean(BoostrapPlaygroundData.class).populate();
        ctx.getBean(BoostrapPlaygroundData.class).retrieve();
    }

    @Transactional
    public void populate() {
        ListThing list = new ListThing("The `equals/hashCode` conundrum");
        ListItemThing li1 = new ListItemThing("application `uuid id` generation (instead of letting hibernate/database generate it)");
        ListItemThing li2 = new ListItemThing("go with `Object` implementations");
        ListItemThing li3 = new ListItemThing("do a per class implementation based on `instanceof` and the `id` field, and just don't put non-persisted entities in collections");
        ListItemThing li4 = new ListItemThing("as above, but create a `@MappedSuperclass` for single implementation and use a single sequence for all tables, [mind the disadvantages](https://stackoverflow.com/questions/1536479/asking-for-opinions-one-sequence-for-all-tables)");

        list.addListItem(li1);
        list.addListItem(li2);
        list.addListItem(li3);
        list.addListItem(li4);

        listRepository.save(list);
        listItemRepository.save(li1);
        listItemRepository.save(li2);
        listItemRepository.save(li3);
        listItemRepository.save(li4);

        retrieve();
        System.err.println("--------------------------------------");
    }

    @Transactional
    public void retrieve() {
        ListThing list = listRepository.findAll().iterator().next();
        System.err.println(list.getShortDesc());

        for (ListItemThing item : list.getChildren()) {
            System.err.println(" - " + item.getItemContents());
        }
    }
}
