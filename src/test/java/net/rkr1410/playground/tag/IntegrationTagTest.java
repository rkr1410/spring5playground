package net.rkr1410.playground.tag;

import net.rkr1410.playground.thing.IntegrationThingTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import static net.rkr1410.playground.thing.ThingType.LIST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class IntegrationTagTest {

    @Autowired private TestEntityManager em;


    @Test
    @DisplayName("Tag value can't be null")
    void testTagValueIsRequired() {
        Tag tag = new Tag(null);

        em.persist(tag);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("must not be null")
                .hasStackTraceContaining("value");
    }

    @Test
    @DisplayName("Tag value can't be longer than 255 characters")
    void testTagValueCantGoAbove255Characters() {
        Tag tag = new Tag("ź".repeat(256));

        em.persist(tag);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("size must be between 0 and 255")
                .hasStackTraceContaining("value");
    }

}
