package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.model.ToyCategory;

public class ToyMapper {
    private ToyMapper() {
        throw new UnsupportedOperationException("ToyMapper is a utility class.");
    }

    public static ToyDto toDto(Toy entity) {
        return new ToyDto(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            StringFormatter.formatToDisplay(entity.getCategory().getName()),
            entity.getImageUrl()
        );
    }

    public static Toy toEntity(ToyDto dto, ToyCategory category) {
        return new Toy(
            dto.getId(),
            dto.getName(),
            dto.getDescription(),
            dto.getPrice(),
            category,
            dto.getImageUrl()
        );
    }
}
