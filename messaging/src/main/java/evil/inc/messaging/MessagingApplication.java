package evil.inc.messaging;

import com.github.javafaker.Faker;
import evil.inc.messaging.dto.DogAdoptionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@SpringBootApplication
public class MessagingApplication {

    public static final String TOPIC = "dog-adoption-request";

    public static void main(String[] args) {
        SpringApplication.run(MessagingApplication.class, args);
    }

    @Bean
    ApplicationRunner sender(KafkaTemplate<String, DogAdoptionRequest> kafkaTemplate) {
        return args -> {
            Faker faker = new Faker();
            kafkaTemplate.send(TOPIC, new DogAdoptionRequest(faker.random().nextInt(100), faker.dog().name()));
        };
    }

    @KafkaListener(topics = TOPIC, groupId = "my-group")
    public void listen(DogAdoptionRequest adoptionRequest) {
        log.info("Received dog adoption request: {}", adoptionRequest);
    }


}
