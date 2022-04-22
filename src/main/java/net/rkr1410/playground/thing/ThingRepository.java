package net.rkr1410.playground.thing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ThingRepository extends JpaRepository<Thing, Long> {

    @Query("SELECT lst FROM Thing lst where lst.type = net.rkr1410.playground.thing.ThingType.LIST")
    Set<Thing> getLists();
}
