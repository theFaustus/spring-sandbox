package evil.inc.web.rest;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.dto.User;
import evil.inc.web.rest.hateoas.UserModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class HateoasUsersController {

    private final DeclarativeUsersClient declarativeUsersClient;
    private final UserModelAssembler userModelAssembler;

    public HateoasUsersController(DeclarativeUsersClient declarativeUsersClient,
                                  UserModelAssembler userModelAssembler) {
        this.declarativeUsersClient = declarativeUsersClient;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable Integer id) {
        return declarativeUsersClient.getUser(id).map(userModelAssembler::toModel).orElse(null);
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getUsers() {
        return userModelAssembler.toCollectionModel(declarativeUsersClient.getUsers());
    }
}
