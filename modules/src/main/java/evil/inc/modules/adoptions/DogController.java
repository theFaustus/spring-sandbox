package evil.inc.modules.adoptions;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dogs")
class DogController {

    private final DogAdoptionService dogAdoptionService;

    DogController(DogAdoptionService dogAdoptionService) {
        this.dogAdoptionService = dogAdoptionService;
    }

    @PostMapping("{dogId}/adoptions")
    void adopt(@PathVariable int dogId, @RequestParam String owner) {
        dogAdoptionService.adopt(dogId, owner);
    }
}
