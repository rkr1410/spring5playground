package net.rkr1410.custom_repo_method.name;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomNameRepositoryImpl implements CustomNameRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override public Name findJoe() {
        return entityManager.createQuery("select name from Name name where name.value = 'Joe'", Name.class).getSingleResult();
    }
}
