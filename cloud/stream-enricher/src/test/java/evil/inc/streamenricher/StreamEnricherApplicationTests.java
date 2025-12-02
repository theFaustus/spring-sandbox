package evil.inc.streamenricher;

import evil.inc.streamenricher.domain.ApprovalStatus;
import evil.inc.streamenricher.domain.CardHolderData;
import evil.inc.streamenricher.domain.CashCard;
import evil.inc.streamenricher.domain.EnrichedTransaction;
import evil.inc.streamenricher.domain.Transaction;
import evil.inc.streamenricher.service.EnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class StreamEnricherApplicationTests {

    @MockitoBean
    private EnrichmentService enrichmentService;

    @Test
    void enrichTransaction(@Autowired InputDestination inputDestination, @Autowired OutputDestination outputDestination) throws IOException {
        Transaction transaction = new Transaction(1L, new CashCard(123L, "Kumar Patel", 1.00));
        EnrichedTransaction enrichedTransaction = new EnrichedTransaction(transaction.id(), transaction.cashCard(),
                ApprovalStatus.APPROVED,
                new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main Street"));
        when(enrichmentService.enrich(transaction)).thenReturn(enrichedTransaction);
        Message<Transaction> message = MessageBuilder.withPayload(transaction).build();
        inputDestination.send(message, "enrichTransaction-in-0");

        Message<byte[]> result = outputDestination.receive(5000, "enrichTransaction-out-0");

        assertThat(result).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        EnrichedTransaction receivedData = objectMapper.readValue(result.getPayload(), EnrichedTransaction.class);
        assertThat(receivedData).isEqualTo(enrichedTransaction);
    }


}
