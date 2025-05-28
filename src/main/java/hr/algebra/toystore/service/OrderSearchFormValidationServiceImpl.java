package hr.algebra.toystore.service;

import hr.algebra.toystore.model.OrderSearchForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderSearchFormValidationServiceImpl implements OrderSearchFormValidationService {
    @Override
    public boolean isValid(OrderSearchForm form) {
        if (form.getFromDate() != null && form.getToDate() != null) {
            return !form.getFromDate().isAfter(form.getToDate());
        }
        return true;
    }
}
