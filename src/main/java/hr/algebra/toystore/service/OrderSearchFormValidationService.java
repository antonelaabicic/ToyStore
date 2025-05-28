package hr.algebra.toystore.service;

import hr.algebra.toystore.model.OrderSearchForm;

public interface OrderSearchFormValidationService {
    boolean isValid(OrderSearchForm form);
}
