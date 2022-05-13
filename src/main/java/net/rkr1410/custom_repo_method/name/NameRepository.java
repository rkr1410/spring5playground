package net.rkr1410.custom_repo_method.name;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NameRepository extends JpaRepository<Name, Long>, CustomNameRepository {

    @Query("select name from Name name where name.value = 'Esther'")
    Name findEsther();
}
