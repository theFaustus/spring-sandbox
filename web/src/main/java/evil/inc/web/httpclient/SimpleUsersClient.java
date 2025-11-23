package evil.inc.web.httpclient;

import evil.inc.web.httpclient.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;

@Component
public class SimpleUsersClient {

    private final RestClient usersRestClient;

    public SimpleUsersClient(RestClient usersRestClient) {
        this.usersRestClient = usersRestClient;
    }

    public Collection<User> getUsers() {
        return usersRestClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}

