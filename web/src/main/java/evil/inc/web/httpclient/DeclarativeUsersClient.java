package evil.inc.web.httpclient;

import evil.inc.web.httpclient.dto.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Collection;
import java.util.Optional;

public interface DeclarativeUsersClient {

    @GetExchange("/users")
    Collection<User> getUsers();

    @GetExchange("/users/{id}")
    Optional<User> getUser(@PathVariable Integer id);
}
