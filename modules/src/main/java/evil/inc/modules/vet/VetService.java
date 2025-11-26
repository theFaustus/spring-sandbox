package evil.inc.modules.vet;

import com.github.javafaker.Faker;
import evil.inc.modules.adoptions.DogAdoptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
class VetService {

    @ApplicationModuleListener()
    void schedule(DogAdoptionEvent dogAdoptionEvent) throws InterruptedException {
        Thread.sleep(5000);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (random.nextBoolean()) {
            throw new RuntimeException("Oops! Failed to schedule dog adoption for " + dogAdoptionEvent);
        }
        Faker faker = new Faker();
        Date appointmentDate = faker.date().between(Date.from(Instant.now()), Date.from(LocalDateTime.now().plusYears(3).toInstant(ZoneOffset.ofHours(2))));
        log.info("Scheduled appointment for {} on {}", dogAdoptionEvent, LocalDate.ofInstant(appointmentDate.toInstant(), ZoneId.systemDefault()));
    }
}
