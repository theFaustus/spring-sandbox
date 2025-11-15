package evil.inc.data.repo;

import evil.inc.data.model.Person;

import java.util.Collection;

public interface BadPersonRepository {
    Collection<Person> findAll();
}
