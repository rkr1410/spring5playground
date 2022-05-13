package net.rkr1410.custom_repo_method;

import net.rkr1410.custom_repo_method.name.Name;
import net.rkr1410.custom_repo_method.name.NameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootstrapCustomRepoMethodData implements CommandLineRunner {

    private final NameRepository nameRepository;

    public BootstrapCustomRepoMethodData(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    @Override public void run(String... args) {
        nameRepository.saveAll(List.of(new Name("Joe"), new Name("Esther")));

        System.err.println(nameRepository.findJoe()); // from custom repo with entity manager
        System.err.println(nameRepository.findEsther()); // from regular repo with @Query annotated interface method
    }
}
