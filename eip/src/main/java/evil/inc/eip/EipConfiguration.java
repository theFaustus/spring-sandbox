package evil.inc.eip;

import evil.inc.eip.domain.PurchaseOrder;
import evil.inc.eip.domain.ShippableLineItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerFactory;
import org.springframework.integration.file.dsl.FileInboundChannelAdapterSpec;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class EipConfiguration {

    @Bean
    DirectChannelSpec inbound() {
        return MessageChannels.direct();
    }

    @Bean
    IntegrationFlow fileInboundFlow(MessageChannel inbound, @Value("classpath:purchase-orders") File file) {
        FileInboundChannelAdapterSpec inboundChannelAdapterSpec = Files.inboundAdapter(file)
                .autoCreateDirectory(true);
        return IntegrationFlow
//                .from(inboundChannelAdapterSpec, pc -> pc.poller(pm -> pm.cron("* * * * *")))
                .from(inboundChannelAdapterSpec, pc -> pc.poller(pm -> PollerFactory.fixedRate(Duration.ofMillis(100))))
                .transform(new FileToStringTransformer())
                .channel(inbound)
                .get();
    }

    @Bean
    IntegrationFlow errorIntegrationFlow(@Qualifier("errorChannel") MessageChannel messageChannel) {
        return IntegrationFlow
                .from(messageChannel)
                .handle((payload, headers) -> {
                    log.error("error={}", payload);
                    return null;
                }).get();
    }

    @Bean
    IntegrationFlow integrationFlow(MessageChannel inbound) {
        return IntegrationFlow
                .from(inbound)
                .transform(new JsonToObjectTransformer(PurchaseOrder.class))
                .filter(PurchaseOrder.class, source -> source.total() > 100)
                .handle((GenericHandler<PurchaseOrder>) (payload, headers) -> {
                    log.info("payload={}", payload);
                    log.info("headers={}", headers.keySet());
                    return payload;
                })
                .transform((GenericTransformer<PurchaseOrder, List<ShippableLineItem>>) source -> {
                    return source.lineItems().stream()
                            .map(line -> new ShippableLineItem(source, line, source.country().equals("USA"), false))
                            .toList();
                })
                .split()
                .handle((GenericHandler<ShippableLineItem>) (payload, headers) -> {
                    log.info("payload={}", payload);
                    log.info("headers={}", headers.keySet());
                    return null;
                })
                .get();
    }

}
