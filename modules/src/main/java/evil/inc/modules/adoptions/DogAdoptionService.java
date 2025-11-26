package evil.inc.modules.adoptions;

import evil.inc.modules.adoptions.domain.Dog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class DogAdoptionService {

    private final DogRepository dogRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    DogAdoptionService(DogRepository dogRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.dogRepository = dogRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    void adopt(int dogId, String owner) {
        dogRepository.findById(dogId).ifPresent(dog -> {
            Dog updated = dogRepository.save(new Dog(dog.id(), dog.name(), owner, dog.description()));
            applicationEventPublisher.publishEvent(new DogAdoptionEvent(updated.id()));
            log.info("Dog adopted with id {} and owner {}", dog.id(), owner);
        });
    }

}
