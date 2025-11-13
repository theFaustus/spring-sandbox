package evil.inc.misc.bpp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        for (String beanName : beanFactory.getBeanNamesForType(Loggable.class)) {
            BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
            Class<?> type = beanFactory.getType(beanName);
            if (Loggable.class.isAssignableFrom(type)) {
                log.info("bean {} is a {} and also a {}. The scope is {}", beanName, type.getSimpleName(), Loggable.class.getName(), beanDef.getScope());
            }
        }
    }
}
