package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.model.ToyCategory;

public class ToyCategoryMapper {
    private ToyCategoryMapper() {
        throw new UnsupportedOperationException("ToyCategoryMapper is a utility class.");
    }

    public static ToyCategoryDto toDto(ToyCategory entity) {
        return new ToyCategoryDto(
            entity.getId(),
            StringFormatter.formatToDisplay(entity.getName())
        );
    }

    public static ToyCategory toEntity(ToyCategoryDto dto) {
        return new ToyCategory(
            dto.getId(),
            StringFormatter.formatToDb(dto.getName())
        );
    }
}
