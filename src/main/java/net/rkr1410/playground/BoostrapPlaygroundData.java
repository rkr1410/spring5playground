package net.rkr1410.playground;

import net.rkr1410.playground.tag.Tag;
import net.rkr1410.playground.tag.TagRepository;
import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.thing.ThingType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.rkr1410.playground.thing.ThingType.LIST;
import static net.rkr1410.playground.thing.ThingType.LIST_ITEM;

@Component
public class BoostrapPlaygroundData implements CommandLineRunner {

    private final EntityManager em;
    private final ApplicationContext ctx;

    public BoostrapPlaygroundData(EntityManager em, ApplicationContext ctx) {
        this.em = em;
        this.ctx = ctx;
    }

    @Override public void run(String... args) {
        ctx.getBean(BoostrapPlaygroundData.class).populate();
        ctx.getBean(BoostrapPlaygroundData.class).retrieve();
    }

    @Transactional
    public void populate() {
        new ThingCreator(LIST, "The `equals/hashCode` conundrum")
                .witchChild(new ThingCreator(LIST_ITEM, "application `uuid id` generation (instead of letting hibernate/database generate it)"))
                .witchChild(new ThingCreator(LIST_ITEM, "go with `Object` implementations"))
                .witchChild(new ThingCreator(LIST_ITEM, "do a per class implementation based on `instanceof` and the `id` field, and just don't put non-persisted entities in collections"))
                .witchChild(new ThingCreator(LIST_ITEM, "as above, but create a `@MappedSuperclass` for single implementation and use a single sequence for all tables, [mind the disadvantages](https://stackoverflow.com/questions/1536479/asking-for-opinions-one-sequence-for-all-tables)"))
                .persistThing(em);
        retrieve();
    }

    @Transactional
    public void retrieve() {
        Thing list = (Thing) em.createQuery("SELECT t from Thing t" +
                        " WHERE t.type = net.rkr1410.playground.thing.ThingType" +
                        ".LIST")
                .getSingleResult();
        System.err.println(list.getShortDesc());

        for (Thing item : list.getChildren()) {
            System.err.println(" - " + item.getShortDesc());
        }
    }

    static class ThingCreator {
        Thing parent;
        ThingType type;
        String shortDesc;
        List<String> tags = new ArrayList<>();
        List<ThingCreator> children = new ArrayList<>();

        public ThingCreator(ThingType type, String shortDesc) {
            this.type = type;
            this.shortDesc = shortDesc;
        }

        @SuppressWarnings("unused") ThingCreator withTag(String tagName) {
            tags.add(tagName);
            return this;
        }

        ThingCreator withParent(Thing aParentThing) {
            parent = aParentThing;
            return this;
        }

        ThingCreator witchChild(ThingCreator child) {
            children.add(child);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Thing persistThing(EntityManager em) {
            return persistThing(em, null);
        }

        public Thing persistThing(EntityManager em, TagRepository tr) {
            Thing thing = new Thing();
            thing.setParent(parent);
            thing.setType(type);
            thing.setShortDesc(shortDesc);
            persistTagsIfPresent(tr).forEach(thing::addTag);

            em.persist(thing);

            children.stream()
                    .map(c -> c.withParent(thing))
                    .forEach(c -> thing.addChild(c.persistThing(em, tr)));
            return thing;
        }

        private List<Tag> persistTagsIfPresent(TagRepository tr) {
            if (!tags.isEmpty() && tr == null) {
                throw new IllegalStateException("Tags not empty, call createThing" +
                        "(TagRepository) instead");
            }
            return tags.stream()
                    .map(tagName -> tr.save(new Tag(tagName)))
                    .collect(Collectors.toList());
        }
    }
}
