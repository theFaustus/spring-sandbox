package evil.inc.streamenricher.domain;

public record EnrichedTransaction(Long id, CashCard cashCard, ApprovalStatus approvalStatus, CardHolderData cardHolderData) {
}
