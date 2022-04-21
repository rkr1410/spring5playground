package net.rkr1410.playground.tlist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ListThingRepository extends JpaRepository<ListThing, Long> {
}
