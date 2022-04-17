package net.rkr1410.playground.pokemon;

import net.rkr1410.playground.domain.pokemon.Type;

import javax.persistence.*;

@Entity
@Table(name = "pokemon", uniqueConstraints = {@UniqueConstraint(name =
        "pokemon_name_uq", columnNames = {"name"})})
public class PokemonEntity {
    @Id
    @GeneratedValue // this should default to (strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    public PokemonEntity() {
    }

    public PokemonEntity(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "PokemonEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
