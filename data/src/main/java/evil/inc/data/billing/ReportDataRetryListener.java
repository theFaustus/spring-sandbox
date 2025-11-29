package evil.inc.data.billing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReportDataRetryListener implements RetryListener {
    @Override
    public <T, E extends Throwable> void onSuccess(RetryContext context, RetryCallback<T, E> callback, T result) {
        log.info("Retrying with context: {} and result: {}", context, result);
        RetryListener.super.onSuccess(context, callback, result);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.error("Failed retrying with context: {}", context, throwable);
        RetryListener.super.onError(context, callback, throwable);
    }
}
