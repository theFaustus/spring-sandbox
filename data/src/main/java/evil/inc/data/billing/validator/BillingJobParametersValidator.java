package evil.inc.data.billing.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.ObjectUtils;

public class BillingJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if(ObjectUtils.isEmpty(parameters.getString("input.file"))) {
            throw new JobParametersInvalidException("Missing required parameter: input.file");
        }
    }
}
