package evil.inc.data.billing;

import evil.inc.data.model.BillingData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@StepScope
@Component
public class BillingDataSkipListener implements SkipListener<BillingData, BillingData> {

    private final Path skippedItemsFile;

    public BillingDataSkipListener(@Value("#{jobParameters['skip.file']}") String skippedItemsFile) {
        this.skippedItemsFile = Paths.get(skippedItemsFile);
    }

    @Override
    public void onSkipInRead(Throwable t) {
        if (t instanceof FlatFileParseException exception) {
            String input = exception.getInput();
            int lineNumber = exception.getLineNumber();
            String skippedLine = lineNumber + "|" + input + System.lineSeparator();
            try {
                log.info("Skipping billing data reading for line: {} with report in {}", skippedLine, skippedItemsFile);
                Files.writeString(skippedItemsFile, skippedLine, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException e) {
                log.error("Error while writing skipped items file", e);
                throw new RuntimeException("Unable to write skipped item " + skippedLine);
            }
        }
    }
}
