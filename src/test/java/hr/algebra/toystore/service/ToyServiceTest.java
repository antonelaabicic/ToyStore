package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.model.ToyCategory;
import hr.algebra.toystore.model.ToySearchForm;
import hr.algebra.toystore.repository.ToyCategoryRepository;
import hr.algebra.toystore.repository.ToyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToyServiceTest {

    @Mock
    private ToyRepository toyRepository;

    @Mock
    private ToyCategoryRepository toyCategoryRepository;

    @InjectMocks
    private ToyServiceImpl toyService;

    @Test
    @DisplayName("findAll -> returns all toys")
    void findAllSuccess() {
        Toy toy = toy("Teddy", "Soft", BigDecimal.TEN, category(1, "PLUSHIES"), "/img.png");

        when(toyRepository.findAll()).thenReturn(List.of(toy));

        List<ToyDto> result = toyService.findAll();
        assertEquals(1, result.size());

        ToyDto dto = result.get(0);

        assertEquals("Teddy", dto.getName());
        assertEquals("Soft", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertEquals("Plushies", dto.getCategoryString());
        assertEquals("/img.png", dto.getImageUrl());

        verify(toyRepository).findAll();
    }

    @Test
    @DisplayName("findById -> returns existing toy")
    void findByIdSuccess() {
        Toy toy = toy("Teddy", "Soft", BigDecimal.TEN, category(1, "PLUSHIES"), "/img.png");

        when(toyRepository.findById(1)).thenReturn(Optional.of(toy));

        Optional<ToyDto> result = toyService.findById(1);

        assertTrue(result.isPresent());

        ToyDto dto = result.get();

        assertEquals("Teddy", dto.getName());
        assertEquals("Soft", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertEquals("Plushies", dto.getCategoryString());
        assertEquals("/img.png", dto.getImageUrl());

        verify(toyRepository).findById(1);
    }

    @Test
    @DisplayName("findById -> returns empty optional")
    void findByIdNotFound() {
        when(toyRepository.findById(1)).thenReturn(Optional.empty());

        Optional<ToyDto> result = toyService.findById(1);

        assertTrue(result.isEmpty());
        verify(toyRepository).findById(1);
    }

    @Test
    @DisplayName("save -> stores toy with default image")
    void saveWithoutImage() {
        ToyCategory category = category(1, "PLUSHIES");
        ToyDto dto = toyDto("Teddy", "Soft", BigDecimal.TEN, "PLUSHIES");

        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.save(dto, null);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);
        verify(toyRepository).save(captor.capture());

        Toy saved = captor.getValue();

        assertEquals("Teddy", saved.getName());
        assertEquals("Soft", saved.getDescription());
        assertEquals(BigDecimal.TEN, saved.getPrice());
        assertEquals(category, saved.getCategory());
        assertEquals("/images/snake.png", saved.getImageUrl());
        verify(toyCategoryRepository).findByName("PLUSHIES");
    }

    @Test
    @DisplayName("save -> throws when category does not exist")
    void saveCategoryMissing() {
        ToyDto dto = toyDto("Teddy", null, null, "PLUSHIES");

        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> toyService.save(dto, null)
        );

        assertEquals("Category not found.", exception.getMessage());
        verify(toyCategoryRepository).findByName("PLUSHIES");
        verify(toyRepository, never()).save(any(Toy.class));
    }

    @Test
    @DisplayName("deleteById -> deletes toy")
    void deleteSuccess() {
        toyService.deleteById(5);
        verify(toyRepository).deleteById(5);
    }

    @Test
    @DisplayName("update -> throws when toy does not exist")
    void updateMissingToy() {
        ToyDto dto = new ToyDto();

        when(toyRepository.findById(5)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> toyService.update(5, dto, null)
        );

        assertEquals("Toy not found.", exception.getMessage());
        verify(toyRepository).findById(5);
        verify(toyRepository, never()).save(any(Toy.class));
    }

    @Test
    @DisplayName("update -> keeps existing image")
    void updateSuccessKeepImage() {
        ToyCategory category = category(1, "PLUSHIES");
        Toy existing = toy("Old", "Old", BigDecimal.ONE, category, "/old.png");
        existing.setId(5);

        ToyDto dto = toyDto("New", "New description", BigDecimal.TEN, "PLUSHIES");

        when(toyRepository.findById(5)).thenReturn(Optional.of(existing));
        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.update(5, dto, null);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);

        verify(toyRepository).save(captor.capture());

        Toy saved = captor.getValue();

        assertEquals("New", saved.getName());
        assertEquals("New description", saved.getDescription());
        assertEquals(BigDecimal.TEN, saved.getPrice());
        assertEquals("/old.png", saved.getImageUrl());
        assertEquals(category, saved.getCategory());
        assertEquals(5, saved.getId());
        verify(toyRepository).findById(5);
    }

    @Test
    @DisplayName("update -> replaces image with dto image")
    void updateUsesDtoImage() {
        ToyCategory category = category(1, "PLUSHIES");

        Toy existing = toy("Old", "Old", BigDecimal.ONE, category, "/old.png" );
        existing.setId(5);

        ToyDto dto = toyDto("New", "New description", BigDecimal.TEN, "PLUSHIES", "/new.png");

        when(toyRepository.findById(5)).thenReturn(Optional.of(existing));
        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.update(5, dto, null);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);
        verify(toyRepository).save(captor.capture());

        Toy saved = captor.getValue();
        assertEquals("New", saved.getName());
        assertEquals("New description", saved.getDescription());
        assertEquals(BigDecimal.TEN, saved.getPrice());
        assertEquals(category, saved.getCategory());
        assertEquals("/new.png", saved.getImageUrl());
        verify(toyRepository).findById(5);
        verify(toyCategoryRepository).findByName("PLUSHIES");
    }

    @Test
    @DisplayName("findByCriteria -> applies all search filters")
    void findByCriteriaAllFilters() {
        ToyCategory plush = category(1, "PLUSHIES");
        ToyCategory cars = category(2, "CARS");

        Toy matching = toy("Teddy Bear", "Very soft plush toy", BigDecimal.valueOf(20), plush);
        Toy wrongName = toy("Race Car", "Very soft plush toy", BigDecimal.valueOf(20), plush);
        Toy wrongDescription = toy("Teddy Bear", "Plastic toy", BigDecimal.valueOf(20), plush);
        Toy tooCheap = toy("Teddy Bear", "Very soft plush toy", BigDecimal.valueOf(2), plush);
        Toy tooExpensive = toy("Teddy Bear", "Very soft plush toy", BigDecimal.valueOf(100), plush);
        Toy wrongCategory = toy("Teddy Bear", "Very soft plush toy", BigDecimal.valueOf(20), cars);

        when(toyRepository.findAll()).thenReturn(List.of(matching, wrongName, wrongDescription, tooCheap, tooExpensive, wrongCategory));

        ToySearchForm form = new ToySearchForm();
        form.setName("teddy");
        form.setDescription("soft");
        form.setMinPrice(BigDecimal.valueOf(10));
        form.setMaxPrice(BigDecimal.valueOf(30));
        form.setCategoryString("PLUSHIES");

        List<ToyDto> result = toyService.findByCriteria(form);

        assertEquals(1, result.size());
        assertEquals("Teddy Bear", result.get(0).getName());
        verify(toyRepository).findAll();
    }

    @Test
    @DisplayName("findByCriteria -> ignores blank name")
    void findByCriteriaBlankName() {
        Toy toy = toy("Bear", "Soft", BigDecimal.TEN, category(1, "PLUSHIES"));

        when(toyRepository.findAll()).thenReturn(List.of(toy));

        ToySearchForm form = new ToySearchForm();
        form.setName("");

        assertEquals(1, toyService.findByCriteria(form).size());
    }

    @Test
    @DisplayName("findByCriteria -> ignores blank description")
    void findByCriteriaBlankDescription() {
        Toy toy = toy("Bear", "Soft", BigDecimal.TEN, category(1, "PLUSHIES"));

        when(toyRepository.findAll()).thenReturn(List.of(toy));

        ToySearchForm form = new ToySearchForm();
        form.setDescription("");

        assertEquals(1, toyService.findByCriteria(form).size());
    }

    @Test
    @DisplayName("findByCriteria -> ignores blank category")
    void findByCriteriaBlankCategory() {
        Toy toy = toy("Bear", "Soft", BigDecimal.TEN, category(1, "PLUSHIES"));

        when(toyRepository.findAll()).thenReturn(List.of(toy));

        ToySearchForm form = new ToySearchForm();
        form.setCategoryString("");

        assertEquals(1, toyService.findByCriteria(form).size());
    }

    @Test
    @DisplayName("findByCriteria -> filters by category id")
    void findByCriteriaCategoryId() {
        ToyCategory plush = category(1, "PLUSHIES");
        ToyCategory cars = category(2, "CARS");

        Toy teddy = toy("Teddy", "Soft", BigDecimal.TEN, plush);
        Toy ferrari = toy("Ferrari", "Fast", BigDecimal.TEN, cars);

        when(toyRepository.findAll()).thenReturn(List.of(teddy, ferrari));

        ToySearchForm form = new ToySearchForm();
        form.setCategoryId(2);

        List<ToyDto> result = toyService.findByCriteria(form);

        assertEquals(1, result.size());
        assertEquals("Ferrari", result.get(0).getName());
        verify(toyRepository).findAll();
    }

    @Test
    @DisplayName("save -> uploads image")
    void saveWithImage() {
        ToyCategory category = category(1, "PLUSHIES");
        ToyDto dto = toyDto( "Bear", "Soft", BigDecimal.TEN, "PLUSHIES");

        MockMultipartFile image = new MockMultipartFile("image", "bear.png", "image/png", "abc".getBytes());

        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.save(dto, image);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);

        verify(toyRepository).save(captor.capture());
        Toy saved = captor.getValue();

        assertEquals("Bear", saved.getName());
        assertTrue(saved.getImageUrl().startsWith("/toy-images/"));
        verify(toyCategoryRepository).findByName("PLUSHIES");
    }

    @Test
    @DisplayName("update -> uploaded image overrides dto image")
    void uploadedImageOverridesDtoImage() {
        ToyCategory category = category(1, "PLUSHIES");

        Toy existing = toy("Old", "Old", BigDecimal.ONE, category, "/old.png");
        existing.setId(5);

        ToyDto dto = toyDto("New", "Updated", BigDecimal.TEN, "PLUSHIES", "/dto.png");

        MockMultipartFile image = new MockMultipartFile("image", "new.png", "image/png", "abc".getBytes());

        when(toyRepository.findById(5)).thenReturn(Optional.of(existing));
        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.update(5, dto, image);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);
        verify(toyRepository).save(captor.capture());
        assertTrue(captor.getValue().getImageUrl().startsWith("/toy-images/"));
    }

    @Test
    @DisplayName("update -> uploads new image")
    void updateWithImage() {
        ToyCategory category = category(1, "PLUSHIES");
        Toy existing = toy("Old", "Old", BigDecimal.ONE, category, "/old.png");
        existing.setId(5);

        ToyDto dto = toyDto("New", "Updated", BigDecimal.TEN, "PLUSHIES" );
        MockMultipartFile image = new MockMultipartFile("image", "new.png", "image/png", "abc".getBytes());

        when(toyRepository.findById(5)).thenReturn(Optional.of(existing));
        when(toyCategoryRepository.findByName("PLUSHIES")).thenReturn(Optional.of(category));

        toyService.update(5, dto, image);

        ArgumentCaptor<Toy> captor = ArgumentCaptor.forClass(Toy.class);
        verify(toyRepository).save(captor.capture());
        Toy saved = captor.getValue();

        assertEquals("New", saved.getName());
        assertTrue(saved.getImageUrl().startsWith("/toy-images/"));

        verify(toyRepository).findById(5);
        verify(toyCategoryRepository).findByName("PLUSHIES");
    }

    // HELPERS
    private ToyCategory category(Integer id, String name) {
        return ToyCategory.builder()
                .id(id)
                .name(name)
                .build();
    }

    private Toy toy(String name, String description, BigDecimal price, ToyCategory category, String imageUrl) {
        return Toy.builder()
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .imageUrl(imageUrl)
                .build();
    }

    private Toy toy(String name, String description, BigDecimal price, ToyCategory category) {
        return toy(name, description, price, category, null);
    }

    private ToyDto toyDto(String name, String description, BigDecimal price, String category, String imageUrl) {
        return ToyDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryString(category)
                .imageUrl(imageUrl)
                .build();
    }

    private ToyDto toyDto(String name, String description, BigDecimal price, String category) {
        return toyDto(name, description, price, category, null);
    }
}
