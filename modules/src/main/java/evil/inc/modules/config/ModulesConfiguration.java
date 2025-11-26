package evil.inc.modules.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.locks.DistributedLock;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.modulith.events.ResubmissionOptions;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Profile("!test")
@Configuration
public class ModulesConfiguration {

    @Bean
    LockRegistry lockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    LockRepository lockRepository(DataSource dataSource) {
        return new DefaultLockRepository(dataSource);
    }

    @Bean
    ApplicationRunner retryEvents(IncompleteEventPublications incompleteEventPublications, LockRegistry lockRegistry) {
        return args -> {
            Lock lock = lockRegistry.obtain("incompleteEventPublications");

            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    log.info("Incomplete event publication started");
                    incompleteEventPublications.resubmitIncompletePublications(ResubmissionOptions.defaults());
                } catch (Exception e) {
                    log.error("Error resubmitting IncompleteEventPublications", e);
                } finally {
                    lock.unlock();
                }
            }
        };
    }
}
