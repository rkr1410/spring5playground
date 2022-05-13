package net.rkr1410.playground;

import net.rkr1410.playground.tag.Tag;
import net.rkr1410.playground.tag.TagRepository;
import net.rkr1410.playground.thing.Thing;
import net.rkr1410.playground.thing.ThingType;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ThingCreator {
    private Thing parent;
    private ThingType type;
    private String shortDesc;
    private List<String> tags = new ArrayList<>();
    private List<ThingCreator> children = new ArrayList<>();

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