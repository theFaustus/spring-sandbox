package evil.inc.someclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.restclient.autoconfigure.RestClientSsl;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
public class SomeClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SomeClientApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    /**
     * This is the default, Non-Load-Balanced RestClient.Builder.
     * By providing this bean without the @LoadBalanced annotation, we ensure that
     * internal components like the Eureka Client (which talks to http://localhost:8761/eureka)
     * use a standard client, preventing the "No instances available for localhost" error.
     * @return The standard RestClient.Builder.
     */
    @Bean
    @Primary
    RestClient.Builder nonLoadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    /**
     * Defines a routing function using the Spring Cloud Gateway Server MVC.
     * <p>
     * This acts as a simple client-side gateway:
     * <ul>
     * <li>It matches incoming GET requests to {@code /port}.</li>
     * <li>The {@code .filter(lb("some-service"))} applies client-side load balancing
     * to forward the request to an instance of the service named "some-service".</li>
     * <li>The {@code http()} handler executes the final outbound call.</li>
     * </ul>
     * @return The load-balanced {@code RouterFunction}.
     */
    @Bean
    RouterFunction<ServerResponse> routes(){
        return route()
                .before(BeforeFilterFunctions.rewritePath("/api", "/"))
                .GET("/api/port", http())
                .filter(lb("some-service"))
                .build();
    }

}

@RestController
class LoadBalancedController {

    private final RestClient restClient;

    LoadBalancedController(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    /**
     * Endpoint to demonstrate a load-balanced call to the "some-service".
     * <p>
     * The URI uses the logical service ID {@code http://some-service/port}, which is
     * automatically resolved to an actual instance URL by the load balancer.
     *
     * @return The response body (port number) from an instance of "some-service".
     */
    @GetMapping("/port-ct")
    String hello(){
        return restClient.get().uri("http://some-service/port")
                .retrieve()
                .body(String.class);
    }
}
