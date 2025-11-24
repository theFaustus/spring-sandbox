package evil.inc.web.graphql;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.dto.Address;
import evil.inc.web.httpclient.dto.User;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UsersGraphQLController {

    private final DeclarativeUsersClient declarativeUsersClient;

    public UsersGraphQLController(DeclarativeUsersClient declarativeUsersClient) {
        this.declarativeUsersClient = declarativeUsersClient;
    }

    @QueryMapping
    public Collection<User> getUsers() {
        return declarativeUsersClient.getUsers();
    }

    // Override in batches
    @BatchMapping
    Map<User, Address> address(Collection<User> users) {
        var addresses = new HashMap<User, Address>();
        users.forEach(user -> {addresses.put(user, address(user));});
        return addresses;
    }

//    @SchemaMapping - Override individual - n + 1 problem
    Address address(User user) {
        return new Address("Str. " + user.address().street(), user.address().suite(), user.address().city(), user.address().zipcode(), user.address().geo());
    }
}
