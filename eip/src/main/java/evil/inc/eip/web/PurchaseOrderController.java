package evil.inc.eip.web;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final MessageChannel inbound;

    public PurchaseOrderController(MessageChannel inbound) {
        this.inbound = inbound;
    }

    @PostMapping
    public ResponseEntity<Boolean> createPurchaseOrder(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.ok(inbound.send(MessageBuilder.withPayload(new String(file.getBytes())).build()));
    }
}
