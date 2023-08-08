package pl.wjanek.store.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateItemDto(
    @NotBlank
    String name,
    String description,
    @NotNull
    @Min(0L)
    BigDecimal price
) {

}
