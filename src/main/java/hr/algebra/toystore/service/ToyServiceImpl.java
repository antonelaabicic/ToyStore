package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.model.ToyCategory;
import hr.algebra.toystore.model.ToySearchForm;
import hr.algebra.toystore.repository.ToyCategoryRepository;
import hr.algebra.toystore.repository.ToyRepository;
import hr.algebra.toystore.util.StringFormatter;
import hr.algebra.toystore.util.ToyMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ToyServiceImpl implements ToyService {
    private final ToyRepository toyRepository;
    private final ToyCategoryRepository toyCategoryRepository;

    private static final Path IMAGE_DIR = Paths.get(System.getProperty("user.home"), "Downloads", "toy-images");

    @Override
    public List<ToyDto> findAll() {
        return toyRepository.findAll().stream()
                .map(ToyMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ToyDto> findById(Integer id) {
        return toyRepository.findById(id)
                .map(ToyMapper::toDto);
    }

    @Override
    public void save(ToyDto dto, MultipartFile imageFile) {
        ToyCategory category = findCategoryByName(dto.getCategoryString());
        dto.setImageUrl(uploadImage(imageFile));
        toyRepository.save(ToyMapper.toEntity(dto, category));
    }

    @Override
    public void deleteById(Integer id) {
        toyRepository.deleteById(id);
    }

    @Override
    public void update(Integer id, ToyDto dto) {
        Toy existingToy = toyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Toy not found."));
        ToyCategory category = findCategoryByName(dto.getCategoryString());

        existingToy = Toy.builder()
                .id(existingToy.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(category)
                .imageUrl(dto.getImageUrl())
                .build();

        toyRepository.save(existingToy);
    }

    @Override
    public void updateWithImage(Integer id, ToyDto dto, MultipartFile imageFile) {
        Toy existingToy = toyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Toy not found."));

        String imageUrl = (dto.getImageUrl() == null || dto.getImageUrl().isBlank())
                ? existingToy.getImageUrl()
                : dto.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadImage(imageFile);
        }

        existingToy = Toy.builder()
                .id(existingToy.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(findCategoryByName(dto.getCategoryString()))
                .imageUrl(imageUrl)
                .build();

        toyRepository.save(existingToy);
    }

    @Override
    public List<ToyDto> findByCriteria(ToySearchForm form) {
        List<Toy> toys = toyRepository.findAll();

        List<Toy> filteredToys = toys.stream()
                .filter(toy -> form.getName() == null || form.getName().isBlank()
                        || (toy.getName() != null && toy.getName().toLowerCase().contains(form.getName().toLowerCase())))
                .filter(toy -> form.getDescription() == null || form.getDescription().isBlank()
                        || (toy.getDescription() != null && toy.getDescription().toLowerCase().contains(form.getDescription().toLowerCase())))
                .filter(toy -> form.getMinPrice() == null
                        || (toy.getPrice() != null && toy.getPrice().compareTo(form.getMinPrice()) >= 0))
                .filter(toy -> form.getMaxPrice() == null
                        || (toy.getPrice() != null && toy.getPrice().compareTo(form.getMaxPrice()) <= 0))
                .filter(toy -> form.getCategoryString() == null || form.getCategoryString().isBlank()
                        || (toy.getCategory() != null && form.getCategoryString().equalsIgnoreCase(toy.getCategory().getName())))
                .filter(toy -> form.getCategoryId() == null
                        || (toy.getCategory() != null && form.getCategoryId().equals(toy.getCategory().getId())))
                .toList();

        return filteredToys.stream()
                .map(ToyMapper::toDto)
                .toList();
    }

    @Override
    public List<ToyDto> findByCategoryId(Integer categoryId) {
        return toyRepository.findByCategory_Id(categoryId).stream()
                .map(ToyMapper::toDto)
                .toList();
    }

    private ToyCategory findCategoryByName(String categoryName) {
        return toyCategoryRepository.findByName(StringFormatter.formatToDb(categoryName))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    private String uploadImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty.");
        }

        try {
            Files.createDirectories(IMAGE_DIR);
            String fileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
            Path imagePath = IMAGE_DIR.resolve(fileName);
            Files.copy(imageFile.getInputStream(), imagePath);
            return "/toy-images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}
