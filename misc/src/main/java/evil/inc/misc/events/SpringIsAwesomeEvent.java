package evil.inc.misc.events;

import org.springframework.context.ApplicationEvent;

public class SpringIsAwesomeEvent extends ApplicationEvent {
    public SpringIsAwesomeEvent(Object source) {
        super(source);
    }
}
