package evil.inc.web.httpclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebConfiguration {

    @Bean
    DeclarativeUsersClient declarativeUsersClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory
                .createClient(DeclarativeUsersClient.class);
    }

    @Bean
    HttpServiceProxyFactory httpServiceProxyFactory(RestClient usersRestClient) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(RestClientAdapter.create(usersRestClient))
                .build();
    }


    @Bean
    RestClient usersRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }
}
