package pl.wjanek.store.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

@SpringBootTest(properties = "spring.cache.type=none")
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/clean.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:sql/insert-items.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @WithMockUser
    @Test
    public void shouldCreateSingleItem() throws Exception {
        String body = """
            {
              "name": "Fourth item name",
              "description": "Fourth item description",
              "price": 4.14
            }
            """;
        mockMvc.perform(post("/item").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(body, false));
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "item", "name = 'Fourth item name' AND description = 'Fourth item description' AND price = 4.14")).isEqualTo(
            1);
    }

    @WithMockUser
    @Test
    public void shouldCreateMultipleItems() throws Exception {
        String body = """
            [
                {
                  "name": "Fourth item name",
                  "description": "Fourth item description",
                  "price": 4.14
                },
                {
                  "name": "Fifth item name",
                  "description": "Fifth item description",
                  "price": 5.14
                }
            ]
            """;
        mockMvc.perform(post("/item/batch").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(body, false));
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "item", "name = 'Fourth item name' AND description = 'Fourth item description' AND price = 4.14")).isEqualTo(1);
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "item", "name = 'Fifth item name' AND description = 'Fifth item description' AND price = 5.14")).isEqualTo(1);
    }

    @WithMockUser
    @Test
    public void shouldGetAllItems() throws Exception {
        String expectedJson = """
            {
              "content": [
                {
                  "id": 1,
                  "name": "First item name",
                  "description": "Item description",
                  "price": 3.14
                },
                {
                  "id": 2,
                  "name": "Item name",
                  "description": "Item description",
                  "price": 6.28
                },
                {
                  "id": 3,
                  "name": "Item name",
                  "description": "Third item description",
                  "price": 3.14
                }
              ]
            }
            """;
        mockMvc.perform(get("/item"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson, false));
    }

    @WithMockUser
    @Test
    public void shouldGetSingleItem() throws Exception {
        mockMvc.perform(get("/item/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{'name': 'First item name', 'description': 'Item description', 'price': 3.14}", false));
    }

    @WithMockUser
    @Test
    public void shouldNotGetSingleItemWhenItemWithIdIsNotFound() throws Exception {
        mockMvc.perform(get("/item/10000"))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("shouldModifySingleItemArguments")
    public void shouldModifySingleItem(String json) throws Exception {
        mockMvc.perform(patch("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json, false));
    }

    private static Stream<String> shouldModifySingleItemArguments() {
        return Stream.of(
            "{\"name\": \"New first item name\"}",
            "{\"description\": \"New item description\"}",
            "{\"price\": 2.22}",
            "{\"name\": \"New first item name\", \"description\": \"New item description\"}"
        );
    }

    @WithMockUser
    @Test
    public void shouldNotModifySingleItemWhenItemWithIdIsNotFound() throws Exception {
        mockMvc.perform(patch("/item/10000")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isNotFound());
    }

}
