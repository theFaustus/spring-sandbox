package evil.inc.data.billing;

import evil.inc.data.model.BillingData;
import evil.inc.data.model.ReportingData;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
@EnableConfigurationProperties({BillingCellularPricingProperties.class})
public class BillingJobConfiguration {

    @Bean
    public Job billingJob(JobRepository jobRepository, Step filePreparationStep, Step fileIngestionStep, Step reportGenerationStep) {
        return new JobBuilder("billing-job-2", jobRepository)
                .validator(new BillingJobParametersValidator())
                .start(filePreparationStep)
                .next(fileIngestionStep)
                .next(reportGenerationStep)
                .build();
    }

    @Bean
    public Step filePreparationStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("file-preparation-step", jobRepository)
                .tasklet((contribution, _) -> {
                    JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                    String inputFile = jobParameters.getString("input.file");
                    Path source = Paths.get(inputFile);
                    Path destination = Paths.get("data/staging", source.toFile().getName());
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step fileIngestionStep(JobRepository jobRepository, JdbcTransactionManager transactionManager,
                                  ItemReader<BillingData> billingDataFlatFileItemReader,
                                  ItemWriter<BillingData> billingDataJdbcBatchItemWriter,
                                  BillingDataSkipListener skipListener) {
        return new StepBuilder("file-ingestion", jobRepository)
                .<BillingData, BillingData>chunk(100, transactionManager)
                .reader(billingDataFlatFileItemReader)
                .writer(billingDataJdbcBatchItemWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(10)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Step reportGenerationStep(JobRepository jobRepository, JdbcTransactionManager transactionManager,
                                     ItemReader<BillingData> billingDataJdbcBatchItemReader,
                                     ItemProcessor<BillingData, ReportingData> billingDataItemProcessor,
                                     ItemWriter<ReportingData> reportingDataFlatFileItemWriter, ReportDataRetryListener reportDataRetryListener) {
        return new StepBuilder("report-generation", jobRepository)
                .<BillingData, ReportingData>chunk(100, transactionManager)
                .reader(billingDataJdbcBatchItemReader)
                .processor(billingDataItemProcessor)
                .writer(reportingDataFlatFileItemWriter)
                .faultTolerant()
                .retry(PricingException.class)
                .retryLimit(10)
                .listener(reportDataRetryListener)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<BillingData> billingDataFlatFileItemReader(@Value("#{jobParameters['input.file']}") String inputFile) {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billing-data-file-reader")
                .resource(new FileSystemResource(inputFile))
                .delimited()
                .names("dataYear", "dataMonth", "accountId", "phoneNumber", "dataUsage", "callDuration", "smsCount")
                .targetType(BillingData.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<BillingData> billingDataJdbcBatchItemReader(DataSource dataSource,
                                                                            @Value("#{jobParameters['data.year']}") Integer year,
                                                                            @Value("#{jobParameters['data.month']}") Integer month
    ) {
        //language=sql
        String sql = String.format("select * from BILLING_DATA where DATA_YEAR = %d and DATA_MONTH = %d", year, month);
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billing-data-table-reader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ReportingData> reportingDataFlatFileItemWriter(
            @Value("#{jobParameters['output.file']}") String outputFile
    ) {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .resource(new FileSystemResource(outputFile))
                .name("reporting-data-file-writer")
                .delimited()
                .names("billingData.dataYear", "billingData.dataMonth", "billingData.accountId", "billingData.phoneNumber", "billingData.dataUsage", "billingData.callDuration", "billingData.smsCount", "billingTotal")
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
