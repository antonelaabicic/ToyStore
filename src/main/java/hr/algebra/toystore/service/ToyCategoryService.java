package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.ToyCategoryDto;

import java.util.List;
import java.util.Optional;

public interface ToyCategoryService {
    List<ToyCategoryDto> findAll();
    Optional<ToyCategoryDto> findById(Integer id);
    void save(ToyCategoryDto dto);
    void deleteById(Integer id);
    void update(Integer id, ToyCategoryDto dto);
}
