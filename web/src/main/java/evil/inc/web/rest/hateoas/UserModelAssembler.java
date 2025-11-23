package evil.inc.web.rest.hateoas;

import evil.inc.web.httpclient.dto.User;
import evil.inc.web.rest.HateoasUsersController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User entity) {
        var controller = HateoasUsersController.class;
        var self = linkTo(methodOn(controller).getUsers()).withRel("all");
        var one = linkTo(methodOn(controller).getUser(entity.id())).withSelfRel();
        return EntityModel.of(entity, self, one);
    }
}
