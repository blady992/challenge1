package pl.wjanek.store.item.dto;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record UpdateItemDto(
    String name,
    String description,
    @Min(0)
    BigDecimal price
) {

}
