package net.rkr1410.playground.pokedex;

import net.rkr1410.playground.pokemon.PokemonEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokedex", uniqueConstraints = {@UniqueConstraint(name =
        "pokedex_name_uq", columnNames = {"name"})})
public class PokedexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "pokedex_pokemon",
            joinColumns =
                    {@JoinColumn(name = "pokedex_id", referencedColumnName = "id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "pokemon_id", referencedColumnName = "id")})
    private List<PokemonEntity> entries;


    public PokedexEntity() {
    }

    public PokedexEntity(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PokemonEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<PokemonEntity> entries) {
        this.entries = entries;
    }

    public String toStringWithEntries() {
        return "PokedexEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", entries=" + entries +
                '}';
    }

    @Override public String toString() {
        return "PokedexEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
