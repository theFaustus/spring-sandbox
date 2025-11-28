package evil.inc.data;

import evil.inc.data.model.Person;
import evil.inc.data.repo.BadPersonRepository;
import evil.inc.data.repo.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class DataApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }

    @Bean
    ApplicationRunner init(PersonRepository personRepository) {
        return _ -> {
//            badPersonRepository.findAll().forEach(person -> log.info("Found: {}", person.toString()));
//            personRepository.findAll().forEach(person -> log.info("Found: {}", person.toString()));
//            personRepository.findByName("Bob").ifPresent(person -> log.info("Found by name: {}", person));
//            personRepository.findById(1L).ifPresent(person -> log.info("Found by id: {}", person));
        };
    }

}
