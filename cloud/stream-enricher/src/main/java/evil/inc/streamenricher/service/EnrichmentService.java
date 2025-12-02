package evil.inc.streamenricher.service;

import com.github.javafaker.Faker;
import evil.inc.streamenricher.domain.ApprovalStatus;
import evil.inc.streamenricher.domain.CardHolderData;
import evil.inc.streamenricher.domain.EnrichedTransaction;
import evil.inc.streamenricher.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EnrichmentService {

    public EnrichedTransaction enrich(Transaction transaction) {
        Faker faker = new Faker();
        CardHolderData cardHolderData = new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), faker.address().fullAddress());
        EnrichedTransaction enrichedTransaction = new EnrichedTransaction(transaction.id(), transaction.cashCard(), ApprovalStatus.APPROVED, cardHolderData);
        log.info("Enriched Transaction: {}", enrichedTransaction);
        return enrichedTransaction;
    }
}
