package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.PaymentMethodDto;
import hr.algebra.toystore.repository.PaymentMethodRepository;
import hr.algebra.toystore.util.PaymentMethodMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodDto> getAll() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(PaymentMethodMapper::toDto)
                .toList();
    }

    @Override
    public PaymentMethodDto findByName(String name) {
        return PaymentMethodMapper.toDto(paymentMethodRepository.findByName(name));
    }

    @Override
    public Optional<PaymentMethodDto> findById(Integer id) {
        return paymentMethodRepository.findById(id)
                .map(PaymentMethodMapper::toDto);
    }
}
