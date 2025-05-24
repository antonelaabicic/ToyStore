package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.model.Cart;
import hr.algebra.toystore.model.CartItem;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.repository.CartRepository;
import hr.algebra.toystore.repository.ToyRepository;
import hr.algebra.toystore.util.CartMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ToyRepository toyRepository;

    @Override
    public CartDto getCart(String sessionId) {
        Cart cart = cartRepository.findBySessionId(sessionId).orElseGet(() -> cartRepository.save(new Cart(sessionId)));
        return CartMapper.toDto(cart);
    }

    @Override
    public void addItem(String sessionId, Integer toyId, int quantity) {
        Cart cart = cartRepository.findBySessionId(sessionId).orElseGet(() -> cartRepository.save(new Cart(sessionId)));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getToy().getId().equals(toyId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            Toy toy = toyRepository.findById(toyId).orElseThrow(() -> new IllegalArgumentException("Toy not found with ID: " + toyId));
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .toy(toy)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newCartItem);
        }

        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(String sessionId, Integer itemId, int quantity) {
        Cart cart = cartRepository.findBySessionId(sessionId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cartRepository.save(cart);
    }

    @Override
    public void removeItem(String sessionId, Integer itemId) {
        Cart cart = cartRepository.findBySessionId(sessionId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String sessionId) {
        Cart cart = cartRepository.findBySessionId(sessionId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
