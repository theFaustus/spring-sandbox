package evil.inc.web.mvc;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class ThymeleafController {

    private final DeclarativeUsersClient declarativeUsersClient;

    public ThymeleafController(DeclarativeUsersClient declarativeUsersClient) {
        this.declarativeUsersClient = declarativeUsersClient;
    }

    @GetMapping("/users.html")
    public String getUsers(Model model) {
        Collection<User> users = declarativeUsersClient.getUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
