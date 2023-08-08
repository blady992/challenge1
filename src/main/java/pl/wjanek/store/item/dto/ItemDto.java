package pl.wjanek.store.item.dto;

import java.math.BigDecimal;

public record ItemDto(
    Long id,
    String name,
    String description,
    BigDecimal price
) {

}
