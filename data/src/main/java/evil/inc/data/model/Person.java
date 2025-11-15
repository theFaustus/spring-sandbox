package evil.inc.data.model;

import org.springframework.data.annotation.Id;

import java.util.Set;

public record Person(@Id Long id, String name, Set<Dog> dogs) {
}
