package net.rkr1410.custom_repo_method.name;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Name {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    public Name() {}

    public Name(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return "Name{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
