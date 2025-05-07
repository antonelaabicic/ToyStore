package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.ToySearchForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ToyService {
    List<ToyDto> findAll();
    Optional<ToyDto> findById(Integer id);
    void save(ToyDto dto, MultipartFile imageFile);
    void deleteById(Integer id);
    void update(Integer id, ToyDto dto);
    void updateWithImage(Integer id, ToyDto dto, MultipartFile imageFile);
    List<ToyDto> findByCriteria(ToySearchForm toySearchForm);
    List<ToyDto> findByCategoryId(Integer categoryId);
}
