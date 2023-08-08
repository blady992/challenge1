package pl.wjanek.store.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty("app.rabbitmq.enabled")
public class HttpTraceSendingFilter extends OncePerRequestFilter {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpTrace httpTrace = new HttpTrace(request.getRequestURL().toString(), request.getMethod());
            rabbitTemplate.convertAndSend("traceQueue", objectMapper.writeValueAsString(httpTrace));
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    record HttpTrace(
        String url,
        String method
    ) {}
}
