package net.rkr1410.custom_repo_method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomRepoMethodApplication {


    /*
    Method from Vlad Mihalcea's blog at https://vladmihalcea.com/custom-spring-data-repository/

    1. Create an interface to store your custom methods. No Spring naming convention or annotations needed.
    2. Implement the methods in a class. No special treatment needed (apart from stuff like
    annotating an entity manager with @PersistenceContext)
    3. Create a regular XXXRepository interface, and extend it with the interface from step 1 in addition to regular stuff (like JpaRepository)

    Note: although testing suggests code from steps 1+2 can be named whatever, it's probably still best to stick to 'Repository' suffix.
    And maybe use 'Custom' prefix, too.
     */


    public static void main(String[] args) {
        SpringApplication.run(CustomRepoMethodApplication.class);
    }
}
