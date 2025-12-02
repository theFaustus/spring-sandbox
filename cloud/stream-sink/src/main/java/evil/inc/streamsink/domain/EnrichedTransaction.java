package evil.inc.streamsink.domain;

public record EnrichedTransaction(Long id, CashCard cashCard, ApprovalStatus approvalStatus, CardHolderData cardHolderData) {
}
