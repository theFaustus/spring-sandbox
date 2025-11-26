package evil.inc.modules.adoptions;

import evil.inc.modules.adoptions.domain.Dog;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DogRepository extends ListCrudRepository<Dog,Integer> {

}
