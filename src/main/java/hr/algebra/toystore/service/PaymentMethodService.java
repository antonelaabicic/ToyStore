package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.PaymentMethodDto;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    List<PaymentMethodDto> getAll();
    PaymentMethodDto findByName(String name);
    Optional<PaymentMethodDto> findById(Integer id);
}
