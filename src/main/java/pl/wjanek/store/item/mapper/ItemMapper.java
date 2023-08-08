package pl.wjanek.store.item.mapper;

import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;
import pl.wjanek.store.item.Item;
import pl.wjanek.store.item.dto.CreateItemDto;
import pl.wjanek.store.item.dto.ItemDto;
import pl.wjanek.store.item.dto.UpdateItemDto;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
            item.getId(), item.getName(), item.getDescription(), item.getPrice()
        );
    }

    public Item toItem(CreateItemDto dto) {
        Item item = new Item();
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setPrice(dto.price());
        return item;
    }

    public void updateItem(Item item, UpdateItemDto dto) {
        ofNullable(dto.name()).ifPresent(item::setName);
        ofNullable(dto.description()).ifPresent(item::setDescription);
        ofNullable(dto.price()).ifPresent(item::setPrice);
    }

}
