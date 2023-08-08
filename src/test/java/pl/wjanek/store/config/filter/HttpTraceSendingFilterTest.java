package pl.wjanek.store.config.filter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HttpTraceSendingFilterTest {

    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private RabbitTemplate rabbitTemplate;
    @Captor
    private ArgumentCaptor<String> httpTraceJsonCaptor;

    @Test
    @WithMockUser
    public void shouldTraceHttpRequestToRabbit() throws Exception {
        mockMvc.perform(get("/item"));
        verify(rabbitTemplate).convertAndSend(eq("traceQueue"), httpTraceJsonCaptor.capture());
        JSONAssert.assertEquals(httpTraceJsonCaptor.getValue(), "{\"url\": \"http://localhost/item\", \"method\": \"GET\"}", false);
    }

}
