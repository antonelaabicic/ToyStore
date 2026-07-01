package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.CartDto;
import hr.algebra.toystore.model.Cart;
import hr.algebra.toystore.model.CartItem;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.repository.CartRepository;
import hr.algebra.toystore.repository.ToyRepository;
import hr.algebra.toystore.util.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ToyRepository toyRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("getCart -> returns existing cart")
    void getCartExisting() {
        Cart cart = cart("ABC");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        CartDto result = cartService.getCart("ABC");

        assertEquals("ABC", result.getSessionId());
        verify(cartRepository).findBySessionId("ABC");
        verify(cartRepository, never()).save(any());
    }

    @Test
    @DisplayName("getCart -> creates new cart")
    void getCartCreatesNew() {
        Cart cart = cart("ABC");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDto result = cartService.getCart("ABC");

        assertEquals("ABC", result.getSessionId());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("findBySessionId -> returns existing cart")
    void findBySessionIdExisting() {
        Cart cart = cart("ABC");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        Cart result = cartService.findBySessionId("ABC");

        assertEquals(cart, result);
        verify(cartRepository).findBySessionId("ABC");
    }

    @Test
    @DisplayName("findBySessionId -> creates new cart")
    void findBySessionIdCreatesNew() {
        Cart cart = cart("ABC");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.findBySessionId("ABC");

        assertEquals(cart, result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("addItem -> adds new item")
    void addNewItem() {
        Cart cart = cart("ABC");
        Toy toy = toy(1, "Teddy");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));
        when(toyRepository.findById(1)).thenReturn(Optional.of(toy));

        cartService.addItem("ABC", 1, 2);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);

        verify(cartRepository).save(captor.capture());
        Cart saved = captor.getValue();

        assertEquals(1, saved.getItems().size());

        CartItem item = saved.getItems().get(0);

        assertEquals(toy, item.getToy());
        assertEquals(2, item.getQuantity());
        verify(toyRepository).findById(1);
    }

    @Test
    @DisplayName("addItem -> increases quantity")
    void increaseExistingItemQuantity() {
        Cart cart = cart("ABC");
        Toy toy = toy(1, "Teddy");

        CartItem item = item(1, toy, 2);
        cart.getItems().add(item);

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        cartService.addItem("ABC", 1, 3);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(captor.capture());

        Cart saved = captor.getValue();

        assertEquals(5, saved.getItems().get(0).getQuantity());
        verify(toyRepository, never()).findById(any());
    }

    @Test
    @DisplayName("addItem -> throws when toy missing")
    void addItemToyMissing() {
        Cart cart = cart("ABC");

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));
        when(toyRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cartService.addItem("ABC", 1, 1)
                );

        assertEquals("Toy not found with ID: 1", exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateItemQuantity -> updates quantity")
    void updateItemQuantity() {
        Cart cart = cart("ABC");
        Toy toy = toy(1, "Teddy");

        CartItem item = item(5, toy, 1);
        cart.getItems().add(item);

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        cartService.updateItemQuantity("ABC", 5, 10);

        assertEquals(10, item.getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("removeItem -> removes item")
    void removeItem() {
        Cart cart = cart("ABC");

        Toy teddy = toy(1, "Teddy");
        Toy car = toy(2, "Car");

        cart.getItems().add(item(1, teddy, 2));
        cart.getItems().add(item(2, car, 1));

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        cartService.removeItem("ABC", 1);

        assertEquals(1, cart.getItems().size());
        assertEquals("Car", cart.getItems().get(0).getToy().getName());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("clearCart -> removes all items")
    void clearCart() {
        Cart cart = cart("ABC");

        cart.getItems().add(item(1, toy(1, "Teddy"), 2));
        cart.getItems().add(item(2, toy(2, "Car"), 3));

        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.of(cart));

        cartService.clearCart("ABC");

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("updateItemQuantity -> throws when cart missing")
    void updateItemQuantityCartMissing() {
        when(cartRepository.findBySessionId("ABC")).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cartService.updateItemQuantity("ABC", 1, 5)
                );

        assertEquals(Constants.CART_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    // HELPERS
    private Cart cart(String sessionId) {
        return Cart.builder()
                .sessionId(sessionId)
                .items(new ArrayList<>())
                .build();
    }

    private Toy toy(Integer id, String name) {
        return Toy.builder()
                .id(id)
                .name(name)
                .build();
    }

    private CartItem item(Integer id, Toy toy, int quantity) {
        return CartItem.builder()
                .id(id)
                .toy(toy)
                .quantity(quantity)
                .build();
    }
}