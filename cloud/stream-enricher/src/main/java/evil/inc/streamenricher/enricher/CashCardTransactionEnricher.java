package evil.inc.streamenricher.enricher;

import evil.inc.streamenricher.domain.EnrichedTransaction;
import evil.inc.streamenricher.domain.Transaction;
import evil.inc.streamenricher.service.EnrichmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class CashCardTransactionEnricher {

    @Bean
    public Function<Transaction, EnrichedTransaction> enrichTransaction(EnrichmentService enrichmentService) {
        return enrichmentService::enrich;
    }

}
