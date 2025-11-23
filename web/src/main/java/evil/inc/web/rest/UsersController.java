package evil.inc.web.rest;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.dto.User;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
//
//@Controller
//@ResponseBody
public class UsersController {

    private final DeclarativeUsersClient declarativeUsersClient;

    public UsersController(DeclarativeUsersClient declarativeUsersClient) {
        this.declarativeUsersClient = declarativeUsersClient;
    }

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return declarativeUsersClient.getUsers();
    }
}
