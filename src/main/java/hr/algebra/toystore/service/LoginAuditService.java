package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.LoginAuditDto;

import java.util.List;

public interface LoginAuditService {
    List<LoginAuditDto> findAll();
}
