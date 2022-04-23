package net.rkr1410.playground.thing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class IntegrationThingTest {

    @Autowired private TestEntityManager em;

    @Test
    @DisplayName("Short description can't be null")
    void testShortDescIsRequired() {
        Thing aThing = createAThing();
        aThing.setShortDesc(null);
        em.persist(aThing);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("must not be null")
                .hasStackTraceContaining("shortDesc");
    }

    @Test
    @DisplayName("Type can't be null")
    void testTypeIsRequired() {
        Thing aThing = createAThing();
        aThing.setType(null);
        em.persist(aThing);

        assertThatThrownBy(() -> em.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasStackTraceContaining("must not be null")
                .hasStackTraceContaining("type");
    }

    @Test
    @DisplayName("Parent can be null")
    void testParentIsNotRequired() {
        Thing aThing = createAThing();
        aThing.setParent(null);
        em.persist(aThing);

        assertThatCode(() -> em.flush()).doesNotThrowAnyException();
    }

    private Thing createAThing() {
        Thing aThing = new Thing();
        aThing.setShortDesc("test");
        aThing.setType(ThingType.LIST);
        aThing.addChild(aThing);

        return aThing;
    }

}
