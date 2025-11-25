package evil.inc.eip.domain;

import java.util.List;
import java.util.Set;

public record PurchaseOrder(String orderId, String country, double total, Set<LineItem> lineItems) {
}
