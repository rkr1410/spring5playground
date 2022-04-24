package net.rkr1410.playground.thing;

import net.rkr1410.playground.tag.Tag;
import net.rkr1410.playground.tag.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.rkr1410.playground.thing.ThingType.LIST;
import static net.rkr1410.playground.thing.ThingType.LIST_ITEM;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class IntegrationThingTest {

    @Autowired private TestEntityManager em;
    @Autowired private TagRepository tagRepository;

    @Test
    @DisplayName("Short description can't be null")
    void testShortDescIsRequired() {
        new TestThing(LIST, null)
                .persistThing(em);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("must not be null")
                .hasStackTraceContaining("shortDesc");
    }

    @Test
    @DisplayName("Type can't be null")
    void testTypeIsRequired() {
        new TestThing(null, "a test thing")
                .persistThing(em);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("must not be null")
                .hasStackTraceContaining("type");
    }

    @Test
    @DisplayName("Parent can be null")
    void testParentIsNotRequired() {
        new TestThing(LIST, "a thing with parent " +
                "specifically set to null")
                .withParent(null)
                .persistThing(em);

        assertThatCode(() -> em.flush()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Short description can't be longer than 255 characters")
    void testShortDescCantGoAbove255Characters() {
        new TestThing(LIST, "a".repeat(256))
                .persistThing(em);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("size must be between 0 and 255")
                .hasStackTraceContaining("shortDesc");
    }

    @Test
    @DisplayName("Simple attributes (other than tags and children) are persisted")
    void testSimpleFieldsPersisted() {
        new TestThing(LIST, "a list-item thing with short description")
                .persistThing(em);

        Thing persisted = selectSingleListThing();
        assertThat(persisted.getShortDesc())
                .isEqualTo("a list-item thing with short description");
        assertThat(persisted.getType())
                .isEqualTo(LIST);
    }

    @Test
    @DisplayName("A thing can have children")
    void testAThingCanHaveChildren() {
        new TestThing(ThingType.LIST, "a proper list with children")
                .witchChild(new TestThing(LIST_ITEM, "List item 1"))
                .witchChild(new TestThing(LIST_ITEM, "List item 2"))
                .witchChild(new TestThing(LIST_ITEM, "List item 3"))
                .persistThing(em);

        Thing persisted = selectSingleListThing();
        assertThat(persisted.getChildren())
                .filteredOn(t -> t.getType() == LIST_ITEM)
                .filteredOn(t -> persisted.equals(t.getParent()))
                .map(Thing::getShortDesc)
                .containsOnly("List item 1", "List item 2", "List item 3");
    }

    @Test
    @DisplayName("A thing can be tagged with any tag")
    void testAThingCanBeTagged() {
        new TestThing(LIST, "a list thing")
                .withTag("home")
                .withTag("shopping")
                .persistThing(em, tagRepository);

        Thing persisted = selectSingleListThing();
        assertThat(persisted.getTags().stream().map(Tag::getValue))
                .containsOnly("home", "shopping");
    }

    private Thing selectSingleListThing() {
        return (Thing) em.getEntityManager()
                .createQuery("SELECT t from Thing t" +
                        " WHERE t.type = net.rkr1410.playground.thing.ThingType" +
                        ".LIST")
                .getSingleResult();
    }

    static class TestThing {
        Thing parent;
        ThingType type;
        String shortDesc;
        List<String> tags = new ArrayList<>();
        List<TestThing> children = new ArrayList<>();

        public TestThing(ThingType type, String shortDesc) {
            this.type = type;
            this.shortDesc = shortDesc;
        }

        TestThing withTag(String tagName) {
            tags.add(tagName);
            return this;
        }

        TestThing withParent(Thing aParentThing) {
            parent = aParentThing;
            return this;
        }

        TestThing witchChild(TestThing child) {
            children.add(child);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Thing persistThing(TestEntityManager em) {
            return persistThing(em, null);
        }

        public Thing persistThing(TestEntityManager em, TagRepository tr) {
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
