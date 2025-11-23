package evil.inc.web;

import evil.inc.web.httpclient.DeclarativeUsersClient;
import evil.inc.web.httpclient.SimpleUsersClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    ApplicationRunner init(SimpleUsersClient simpleUsersClient, DeclarativeUsersClient declarativeUsersClient) {
        return args -> {
            simpleUsersClient.getUsers().forEach(u -> log.info("{}::{}", simpleUsersClient.getClass().getSimpleName(), u));
            declarativeUsersClient.getUsers().forEach(u -> log.info("{}::{}", declarativeUsersClient.getClass().getSimpleName(), u));
            declarativeUsersClient.getUser("1").ifPresent(u -> log.info("{}::{}", declarativeUsersClient.getClass().getSimpleName(), u));
        };
    }
}
