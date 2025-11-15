package evil.inc.data.repo;

import evil.inc.data.model.Person;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByName(String name);

    @Query("select * from person p where p.id = :id")
    Optional<Person> findById(@Param("id") Long id);
}
