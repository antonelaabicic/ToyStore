package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.model.ToyCategory;
import hr.algebra.toystore.repository.ToyCategoryRepository;
import hr.algebra.toystore.repository.ToyRepository;
import hr.algebra.toystore.util.ToyCategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToyCategoryServiceImpl implements ToyCategoryService {

    private final ToyCategoryRepository toyCategoryRepository;
    private final ToyRepository toyRepository;

    @Override
    public List<ToyCategoryDto> findAll() {
        return toyCategoryRepository.findAll().stream()
                .map(ToyCategoryMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ToyCategoryDto> findById(Integer id) {
        return toyCategoryRepository.findById(id)
                .map(ToyCategoryMapper::toDto);
    }

    @Override
    public void save(ToyCategoryDto dto) {
        toyCategoryRepository.save(ToyCategoryMapper.toEntity(dto));
    }

    @Override
    public void deleteById(Integer id) {
        List<Toy> toys = toyRepository.findAll()
                .stream()
                .filter(toy -> toy.getCategory() != null && toy.getCategory().getId().equals(id))
                .toList();

        toyRepository.deleteAll(toys);
        toyCategoryRepository.deleteById(id);
    }

    @Override
    public void update(Integer id, ToyCategoryDto dto) {
        ToyCategory existingToyCategory = toyCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Toy category not found."));

        existingToyCategory.setName(dto.getName());
        toyCategoryRepository.save(existingToyCategory);
    }
}
