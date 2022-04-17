package net.rkr1410.playground;

import net.rkr1410.playground.domain.pokemon.Type;
import net.rkr1410.playground.pokedex.PokedexEntity;
import net.rkr1410.playground.pokedex.PokedexRepository;
import net.rkr1410.playground.pokemon.PokemonEntity;
import net.rkr1410.playground.pokemon.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoostrapPlaygroundData implements CommandLineRunner {

    private final PokemonRepository pokemonRepository;
    private final PokedexRepository pokedexRepository;

    public BoostrapPlaygroundData(PokemonRepository pokemonRepository,
                                  PokedexRepository pokedexRepository) {
        this.pokemonRepository = pokemonRepository;
        this.pokedexRepository = pokedexRepository;
    }

    @Override public void run(String... args) throws Exception {
        PokemonEntity p1 = new PokemonEntity("Pikachu", Type.ELECTRIC);
        PokemonEntity p2 = new PokemonEntity("Squirtle", Type.WATER);

        pokemonRepository.save(p1);
        pokemonRepository.save(p2);

        PokedexEntity pokedex = new PokedexEntity("Ash's pokedex");
        pokedex.getEntries().add(p1);
        pokedex.getEntries().add(p2);

        pokedexRepository.save(pokedex);

        List<PokemonEntity> all = pokemonRepository.findAll();
        System.err.println(all);
        List<PokedexEntity> all1 = pokedexRepository.findAll();
        System.err.println(all1);

        pokedexRepository.findAllWithEntries().forEach(e -> System.err.println(e.toStringWithEntries()));


    }
}
