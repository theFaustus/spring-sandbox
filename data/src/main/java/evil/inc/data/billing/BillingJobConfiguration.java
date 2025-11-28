package evil.inc.data.billing;

import evil.inc.data.billing.step.FilePreparationTasklet;
import evil.inc.data.billing.validator.BillingJobParametersValidator;
import evil.inc.data.model.BillingData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BillingJobConfiguration {

    @Bean
    public Job billingJob(JobRepository jobRepository, Step filePreparationStep, Step fileIngestionStep) {
        return new JobBuilder("billing-job", jobRepository)
                .validator(new BillingJobParametersValidator())
                .start(filePreparationStep)
                .next(fileIngestionStep)
                .build();
    }

    @Bean
    public Step filePreparationStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("file-preparation-step", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step fileIngestionStep(JobRepository jobRepository, JdbcTransactionManager transactionManager, ItemReader<BillingData> billingDataFlatFileItemReader, ItemWriter<BillingData> billingDataJdbcBatchItemWriter) {
        return new StepBuilder("file-ingestion", jobRepository)
                .<BillingData, BillingData>chunk(100, transactionManager)
                .reader(billingDataFlatFileItemReader)
                .writer(billingDataJdbcBatchItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<BillingData> billingDataFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billing-data-file-reader")
                .resource(new FileSystemResource("data/staging/billing-2023-01.csv"))
                .delimited()
                .names("dataYear", "dataMonth", "accountId", "phoneNumber", "dataUsage", "callDuration", "smsCount")
                .targetType(BillingData.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BillingData> billingDataJdbcBatchItemWriter(DataSource dataSource) {
        //language=sql
        String sql = "insert into BILLING_DATA values (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";
        return new JdbcBatchItemWriterBuilder<BillingData>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
