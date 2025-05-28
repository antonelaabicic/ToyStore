package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.model.PaymentMethod;

public class PaymentMethodMapper {
    private PaymentMethodMapper() {
        throw new UnsupportedOperationException("PaymentMethodMapper is a utility class.");
    }

    public static PaymentMethodDto toDto(PaymentMethod entity) {
        return new PaymentMethodDto(
                entity.getId(),
                StringFormatter.formatToDisplay(entity.getName())
        );
    }

    public static PaymentMethod toEntity(PaymentMethodDto dto) {
        return new PaymentMethod(
                dto.getId(),
                StringFormatter.formatToDb(dto.getName())
        );
    }
}
