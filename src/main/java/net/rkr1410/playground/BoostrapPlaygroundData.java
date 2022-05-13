package net.rkr1410.playground;

import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.thing.ThingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Objects;

import static net.rkr1410.playground.thing.ThingType.LIST;
import static net.rkr1410.playground.thing.ThingType.LIST_ITEM;

@Component
public class BoostrapPlaygroundData implements CommandLineRunner {

    private final EntityManager em;
    private final ApplicationContext ctx;
    private final ThingRepository thingRepository;

    public BoostrapPlaygroundData(EntityManager em, ApplicationContext ctx,
                                  ThingRepository thingRepository) {
        this.em = em;
        this.ctx = ctx;
        this.thingRepository = thingRepository;
    }

    @Override public void run(String... args) {
        ctx.getBean(BoostrapPlaygroundData.class).populate();
        ctx.getBean(BoostrapPlaygroundData.class).retrieve("Separate transaction");
    }

    @Transactional
    public void populate() {
        new ThingCreator(LIST, "The `equals/hashCode` conundrum")
                .witchChild(new ThingCreator(LIST_ITEM, "application `uuid id` generation (instead of letting hibernate/database generate it)"))
                .witchChild(new ThingCreator(LIST_ITEM, "go with `Object` implementations"))
                .witchChild(new ThingCreator(LIST_ITEM, "do a per class implementation based on `instanceof` and the `id` field, and just don't put non-persisted entities in collections"))
                .witchChild(new ThingCreator(LIST_ITEM, "as above, but create a `@MappedSuperclass` for single implementation and use a single sequence for all tables, [mind the disadvantages](https://stackoverflow.com/questions/1536479/asking-for-opinions-one-sequence-for-all-tables)"))
                .persistThing(em);
        retrieve("The same transaction as save()");
    }

    public void retrieve(String header) {
        printHeader(header);
        Thing list = thingRepository.getListsWithChildren().iterator().next();
        System.err.println(list.getShortDesc());
        list.getChildren().forEach(BoostrapPlaygroundData::printListItem);
        System.err.println();
    }

    static void printHeader(String header) {
        int pad = (150 - Objects.requireNonNull(header, "Header cannot be null!").length()) / 2;
        System.err.println("\u2588".repeat(pad) + " " + header + " " + "\u2588".repeat(pad));
    }

    static void printListItem(Thing li) {
        System.err.println(" - " + li.getShortDesc());
    }
}
