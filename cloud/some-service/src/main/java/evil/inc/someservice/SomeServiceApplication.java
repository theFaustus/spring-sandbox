package evil.inc.someservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@SpringBootApplication
public class SomeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SomeServiceApplication.class, args);
    }

    private final AtomicInteger port = new AtomicInteger(0);

    @EventListener
    void onApplicationEvent(WebServerInitializedEvent event) {
        this.port.set(event.getWebServer().getPort());
    }

    @GetMapping("/port")
    Map<String, String> getPort() {
        return Map.of("port", Integer.toString(port.get()));
    }

}
