package evil.inc.streamsource.stream;

import evil.inc.streamsource.domain.Transaction;
import evil.inc.streamsource.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Slf4j
@Configuration
public class CashCardStreamOnDemand {

    private final StreamBridge streamBridge;

    public CashCardStreamOnDemand(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishOnDemand(Transaction txn) {
        streamBridge.send("approvalRequest-out-0", txn);
    }

}
