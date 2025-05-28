package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.model.Cart;

public interface CartService {
    CartDto getCart(String sessionId);
    Cart findBySessionId(String sessionId);
    void addItem(String sessionId, Integer toyId, int quantity);
    void updateItemQuantity(String sessionId, Integer itemId, int quantity);
    void removeItem(String sessionId, Integer itemId);
    void clearCart(String sessionId);
}
