package evil.inc.data.config;

import evil.inc.data.model.Dog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
//@Configuration
public class SimpleJobConfiguration {

    @Bean
    Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Dog> reader, ItemWriter<Dog> writer) {
        return new StepBuilder("step", jobRepository)
                .<Dog, Dog>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    ItemReader<Dog> reader(@Value("classpath:dogs.csv") Resource resource) {
        return new FlatFileItemReaderBuilder<Dog>()
                .name("dog-reader")
                .resource(resource)
                .delimited().names("id", "name", "owner", "description")
                .linesToSkip(1)
                .fieldSetMapper(fieldSet -> new Dog(
                        fieldSet.readInt("id"),
                        fieldSet.readString("name"),
                        fieldSet.readString("owner"),
                        fieldSet.readString("description")
                ))
                .build();
    }

    @Bean
    ItemWriter<Dog> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Dog>()
                .dataSource(dataSource)
                .sql("INSERT INTO DOG (name, owner, description, person) VALUES (?, ?, ?, ?)")
                .assertUpdates(true)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setString(1, item.name());
                    ps.setString(2, item.owner());
                    ps.setString(3, item.description());
                    ps.setInt(4, 1);
                })
                .build();
    }
}
