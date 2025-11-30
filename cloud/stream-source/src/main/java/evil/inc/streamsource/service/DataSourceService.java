package evil.inc.streamsource.service;

import com.github.javafaker.Faker;
import evil.inc.streamsource.domain.CashCard;
import evil.inc.streamsource.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DataSourceService {

    public Transaction getTransaction() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Faker faker = new Faker();
        CashCard cashCard = new CashCard(
                random.nextLong(),
                faker.name().username(),
                random.nextDouble(100.0)
        );
        Transaction transaction = new Transaction(random.nextLong(), cashCard);
        log.info("Generated transaction: {}", transaction);
        return transaction;
    }
}
