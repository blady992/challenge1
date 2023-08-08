package pl.wjanek.store.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableRabbit
@ConditionalOnProperty("app.rabbitmq.enabled")
public class RabbitConfig {

    public static final String TRACE_QUEUE_NAME = "traceQueue";

    @RabbitListener(queues = TRACE_QUEUE_NAME)
    public void listen(String message) {
        log.trace(message);
    }

}
