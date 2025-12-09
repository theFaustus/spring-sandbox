package evil.inc.security.repo;

import evil.inc.security.domain.CashCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    List<CashCard> findAllByOwner(String owner);
}
