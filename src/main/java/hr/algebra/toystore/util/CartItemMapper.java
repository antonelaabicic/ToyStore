package hr.algebra.toystore.util;


import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.CartItemDto;
import hr.algebra.toystore.model.CartItem;
import hr.algebra.toystore.model.Toy;

import java.util.List;

public class CartItemMapper {
    private CartItemMapper() {
        throw new UnsupportedOperationException("CartItemMapper is a utility class.");
    }

    public static CartItemDto toDto(CartItem entity) {
        return new CartItemDto(
            entity.getId(),
            entity.getToy().getId(),
            entity.getToy().getName(),
            entity.getToy().getPrice(),
            entity.getQuantity(),
            entity.getTotalPriceOfItem()
        );
    }

    public static CartItem toEntity(CartItemDto dto, Toy toy) {
        return CartItem.builder()
            .id(dto.getId())
            .toy(toy)
            .quantity(dto.getQuantity())
            .build();
    }
}
