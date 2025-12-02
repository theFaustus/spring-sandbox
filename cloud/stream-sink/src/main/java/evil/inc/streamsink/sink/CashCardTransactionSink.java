package evil.inc.streamsink.sink;

import evil.inc.streamsink.domain.EnrichedTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class CashCardTransactionSink {

    @Bean
    public Consumer<EnrichedTransaction> sinkToConsole() {
        return transaction -> {
            log.info("Transaction received: {}", transaction);
        };
    }

    @Bean
    public Consumer<EnrichedTransaction> sinkToFile() {
        return enrichedTransaction -> {
            StringJoiner joiner = new StringJoiner(",");
            StringJoiner enrichedTxnTextual = joiner.add(String.valueOf(enrichedTransaction.id()))
                    .add(String.valueOf(enrichedTransaction.cashCard().id()))
                    .add(String.valueOf(enrichedTransaction.cashCard().amountRequestedForAuth()))
                    .add(enrichedTransaction.cardHolderData().name())
                    .add(enrichedTransaction.cardHolderData().userId().toString())
                    .add(enrichedTransaction.cardHolderData().address())
                    .add(enrichedTransaction.approvalStatus().name());
            Path destination = Paths.get("C:\\Users\\ionpa\\Projects\\spring-sandbox\\cloud\\stream-sink\\staging\\transactions.csv");
            try {
                boolean newFile = new File(destination.toString()).createNewFile();
                Files.writeString(destination, enrichedTxnTextual + "\n", StandardOpenOption.APPEND);
                log.info("Transaction {} saved to {}", enrichedTransaction, destination);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
