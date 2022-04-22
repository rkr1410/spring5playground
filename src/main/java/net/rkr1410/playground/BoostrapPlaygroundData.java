package net.rkr1410.playground;

import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.thing.ThingRepository;
import net.rkr1410.playground.thing.ThingType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BoostrapPlaygroundData implements CommandLineRunner {

    private final ApplicationContext ctx;
    private final ThingRepository thingRepository;

    public BoostrapPlaygroundData(ApplicationContext ctx,
                                  ThingRepository thingRepository) {
        this.ctx = ctx;
        this.thingRepository = thingRepository;
    }

    @Override public void run(String... args) throws Exception {
        ctx.getBean(BoostrapPlaygroundData.class).populate();
        ctx.getBean(BoostrapPlaygroundData.class).retrieve();
    }

    @Transactional
    public void populate() {
        Thing list = new Thing();
        list.setType(ThingType.LIST);
        list.setShortDesc("The `equals/hashCode` conundrum");

        Thing li1 = new Thing();
        li1.setType(ThingType.LIST_ITEM);
        li1.setShortDesc("application `uuid id` generation (instead of letting hibernate/database generate it)");
        Thing li2 = new Thing();
        li2.setType(ThingType.LIST_ITEM);
        li2.setShortDesc("go with `Object` implementations");
        Thing li3 = new Thing();
        li3.setType(ThingType.LIST_ITEM);
        li3.setShortDesc("do a per class implementation based on `instanceof` and the `id` field, and just don't put non-persisted entities in collections");
        Thing li4 = new Thing();
        li4.setType(ThingType.LIST_ITEM);
        li4.setShortDesc("as above, but create a `@MappedSuperclass` for single implementation and use a single sequence for all tables, [mind the disadvantages](https://stackoverflow.com/questions/1536479/asking-for-opinions-one-sequence-for-all-tables)");

        list.addChild(li1);
        list.addChild(li2);
        list.addChild(li3);
        list.addChild(li4);

        thingRepository.save(list);
        thingRepository.save(li1);
        thingRepository.save(li2);
        thingRepository.save(li3);
        thingRepository.save(li4);

        retrieve();
        System.err.println("--------------------------------------");
    }

    @Transactional
    public void retrieve() {
        Thing list = thingRepository.getLists().iterator().next();
        System.err.println(list.getShortDesc());

        for (Thing item : list.getChildren()) {
            System.err.println(" - " + item.getShortDesc());
        }
    }
}
