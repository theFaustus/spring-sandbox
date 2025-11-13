package evil.inc.misc.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;

@Slf4j
public class LoggableProxyMaker {

    @SuppressWarnings("unchecked")
    public static <T> T proxy(T target) {
        var pfb = new ProxyFactoryBean();
        pfb.setTarget(target);
        pfb.setProxyTargetClass(true);
        pfb.addAdvice((MethodInterceptor) invocation -> {
            var start = System.currentTimeMillis();
            Object result = null;
            try {
                result = invocation.proceed();
            } finally {
                var duration = System.currentTimeMillis() - start;
                log.info("method {} took {} ms", invocation.getMethod().getName(), duration);
            }
            return result;
        });
        return (T) pfb.getObject();
    }
}
