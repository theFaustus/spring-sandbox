package evil.inc.web.mvc;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.dto.User;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class HtmxController {

    private final DeclarativeUsersClient declarativeUsersClient;

    public HtmxController(DeclarativeUsersClient declarativeUsersClient) {
        this.declarativeUsersClient = declarativeUsersClient;
    }

    @GetMapping("/users-htmx.html")
    public String getUsersPage() {
        return "users-htmx";
    }

    @SneakyThrows
    @GetMapping("/users/table")
    public String getUsersTable(Model model) {
        Thread.sleep(3000);
        Collection<User> users = declarativeUsersClient.getUsers();
        model.addAttribute("users", users);
        return "users-table";
    }
}
