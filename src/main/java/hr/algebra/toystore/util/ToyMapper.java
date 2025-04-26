package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.ToyDto;
import hr.algebra.toystore.model.Toy;
import hr.algebra.toystore.model.ToyCategory;

public class ToyMapper {
    private ToyMapper() {
        throw new UnsupportedOperationException("ToyMapper is a utility class.");
    }

    public static ToyDto toDto(Toy toy) {
        return new ToyDto(
                toy.getId(),
            toy.getName(),
            toy.getDescription(),
            toy.getPrice(),
            toy.getStock(),
                StringFormatter.formatToDisplay(toy.getCategory().getName()),
            toy.getImageUrl()
        );
    }

    public static Toy toEntity(ToyDto dto, ToyCategory category) {
        return new Toy(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStock(),
                category, // probably format isn't needed
                dto.getImageUrl()
        );
    }
}
