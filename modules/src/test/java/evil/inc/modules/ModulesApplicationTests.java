package evil.inc.modules;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class ModulesApplicationTests {

    @Test
    void contextLoads() {

        ApplicationModules verified = ApplicationModules.of(ModulesApplication.class).verify();
        System.out.println(verified);
        new Documenter(verified).writeDocumentation();
    }

}
