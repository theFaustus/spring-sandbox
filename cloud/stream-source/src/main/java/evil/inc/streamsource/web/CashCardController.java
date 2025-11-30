package evil.inc.streamsource.web;

import evil.inc.streamsource.domain.Transaction;
import evil.inc.streamsource.stream.CashCardStream;
import evil.inc.streamsource.stream.CashCardStreamOnDemand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cash-card")
public class CashCardController {

    private final CashCardStreamOnDemand cashCardStream;

    public CashCardController(CashCardStreamOnDemand cashCardStream) {
        this.cashCardStream = cashCardStream;
    }

    @PostMapping("/txn")
    public void publishTxn(@RequestBody Transaction txn) {
        log.info("Publishing transaction {}", txn);
        cashCardStream.publishOnDemand(txn);

    }
}
