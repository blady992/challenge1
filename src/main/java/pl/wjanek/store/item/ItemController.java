package pl.wjanek.store.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.wjanek.store.item.dto.CreateItemDto;
import pl.wjanek.store.item.dto.ItemDto;
import pl.wjanek.store.item.dto.UpdateItemDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    Page<ItemDto> findItems(@PageableDefault Pageable pageable) {
        return itemService.findItems(pageable);
    }

    @GetMapping("/{id}")
    ResponseEntity<ItemDto> findItem(@PathVariable Long id) {
        return itemService.findItem(id).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ItemDto saveItem(@RequestBody @Valid CreateItemDto dto) {
        return itemService.saveItem(dto);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    List<ItemDto> saveItems(@RequestBody @Valid Collection<CreateItemDto> dtos) {
        return itemService.saveItems(dtos);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody @Valid UpdateItemDto dto) {
        return itemService.updateItem(id, dto).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

}
