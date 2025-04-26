package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.ToyCategoryDto;
import hr.algebra.toystore.model.ToyCategory;

public class ToyCategoryMapper {
    private ToyCategoryMapper() {
        throw new UnsupportedOperationException("ToyCategoryMapper is a utility class.");
    }

    public static ToyCategoryDto toDto(ToyCategory toyCategory) {
        return new ToyCategoryDto(
            toyCategory.getId(),
            StringFormatter.formatToDisplay(toyCategory.getName())
        );
    }

    public static ToyCategory toEntity(ToyCategoryDto dto) {
        return new ToyCategory(
            dto.getId(),
            StringFormatter.formatToDb(dto.getName())
        );
    }
}
