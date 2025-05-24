package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.dto.CartItemDto;
import hr.algebra.toystore.model.Cart;
import hr.algebra.toystore.model.CartItem;
import hr.algebra.toystore.model.Toy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartMapper {

    private CartMapper() {
        throw new UnsupportedOperationException("CartMapper is a utility class.");
    }

    public static CartDto toDto(Cart entity) {
        List<CartItemDto> itemDtos = entity.getItems()
            .stream()
            .map(CartItemMapper::toDto)
            .collect(Collectors.toList());

        return new CartDto(
                entity.getId(),
                entity.getSessionId(),
                itemDtos,
                entity.getTotalPrice()
        );
    }

    public static Cart toEntity(CartDto dto, Map<Integer, Toy> toyMap) {
        Cart cart = Cart.builder()
            .id(dto.getId())
            .sessionId(dto.getSessionId())
            .build();

        List<CartItem> items = dto.getItems().stream()
                .map(cartItemDto -> {
                    Toy toy = toyMap.get(cartItemDto.getToyId());
                    CartItem cartItem = CartItemMapper.toEntity(cartItemDto, toy);
                    cartItem.setCart(cart);
                    return cartItem;
                })
                .collect(Collectors.toList());

        cart.setItems(items);
        return cart;
    }
}
