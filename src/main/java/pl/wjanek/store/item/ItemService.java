package pl.wjanek.store.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wjanek.store.item.dto.CreateItemDto;
import pl.wjanek.store.item.dto.ItemDto;
import pl.wjanek.store.item.dto.UpdateItemDto;
import pl.wjanek.store.item.mapper.ItemMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public List<ItemDto> saveItems(Collection<CreateItemDto> dtos) {
        List<Item> items = dtos.stream()
            .map(itemMapper::toItem)
            .toList();
        return itemRepository.saveAll(items).stream()
            .map(itemMapper::toItemDto)
            .toList();
    }

    public ItemDto saveItem(CreateItemDto createItemDto) {
        Item item = itemMapper.toItem(createItemDto);
        item = itemRepository.save(item);
        return itemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDto> findItem(Long id) {
        return itemRepository.findById(id)
            .map(itemMapper::toItemDto);
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> findItems(Pageable pageable) {
        return itemRepository.findAll(pageable)
            .map(itemMapper::toItemDto);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Optional<ItemDto> updateItem(Long id, UpdateItemDto dto) {
        return itemRepository.findById(id)
            .map(item -> {
                itemMapper.updateItem(item, dto);
                return itemRepository.save(item);
            })
            .map(itemMapper::toItemDto);
    }
}
