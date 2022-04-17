package net.rkr1410.playground.pokedex;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PokedexRepository extends JpaRepository<PokedexEntity, Long> {

    @Query(value = "select distinct pd from PokedexEntity pd left join fetch pd.entries")
    List<PokedexEntity> findAllWithEntries();
}
