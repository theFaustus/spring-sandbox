package evil.inc.streamsource;

import evil.inc.streamsource.domain.CashCard;
import evil.inc.streamsource.domain.Transaction;
import evil.inc.streamsource.service.DataSourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
class StreamSourceApplicationTests {

    @MockitoBean
    private DataSourceService dataSourceService;

    @Test
    void basicCashCardSupplierTest(@Autowired OutputDestination outputDestination) throws IOException {
        Transaction expectedTransaction = new Transaction(1L, new CashCard(1L, "mike", 1.00));
        when(dataSourceService.getTransaction()).thenReturn(expectedTransaction);

        Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");
        ObjectMapper mapper = new ObjectMapper();
        Transaction actualTransaction = mapper.readValue(result.getPayload(), Transaction.class);

        assertThat(actualTransaction).isEqualTo(expectedTransaction);
        assertThat(result).isNotNull();
    }

}
