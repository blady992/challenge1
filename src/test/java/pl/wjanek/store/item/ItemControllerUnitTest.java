package pl.wjanek.store.item;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.wjanek.store.item.dto.ItemDto;
import pl.wjanek.store.item.dto.UpdateItemDto;

import java.math.BigDecimal;
import java.util.Optional;

@WebMvcTest(controllers = ItemController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ItemControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @Test
    public void shouldGetSingleItem() throws Exception {
        String json = """
            {
                "id": 1,
                "name": "name",
                "description": "description",
                "price": 1.00
            }
            """;
        ItemDto result = new ItemDto(1L, "name", "description", BigDecimal.ONE);
        when(itemService.findItem(1L)).thenReturn(Optional.of(result));

        mockMvc.perform(get("/item/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(json));
        verify(itemService).findItem(eq(1L));
    }

    @Test
    public void shouldNotGetSingleItemWhenItemIsNotFound() throws Exception {
        when(itemService.findItem(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/item/1"))
            .andExpect(status().isNotFound());
        verify(itemService).findItem(eq(1L));
    }

    @Test
    public void shouldUpdateSingleItem() throws Exception {
        String json = """
            {
                "name": "name",
                "description": "description",
                "price": 1
            }
            """;
        ItemDto result = new ItemDto(1L, "name", "description", BigDecimal.ONE);
        UpdateItemDto dto = new UpdateItemDto("name", "description", BigDecimal.ONE);
        when(itemService.updateItem(eq(1L), eq(dto))).thenReturn(Optional.of(result));

        mockMvc.perform(patch("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().json(json));
        verify(itemService).updateItem(eq(1L), eq(dto));
    }

    @Test
    public void shouldNotUpdateSingleItemWhenItemIsNotFound() throws Exception {
        when(itemService.updateItem(eq(1L), any())).thenReturn(Optional.empty());

        mockMvc.perform(patch("/item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isNotFound());
        verify(itemService).updateItem(eq(1L), any());
    }
}
