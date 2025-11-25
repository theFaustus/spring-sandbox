package evil.inc.eip.domain;

public record ShippableLineItem(PurchaseOrder order, LineItem original, boolean domestic, boolean shipped) {
}
