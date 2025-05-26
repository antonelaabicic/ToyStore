package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.LoginAuditDto;
import hr.algebra.toystore.repository.LoginAuditRepository;
import hr.algebra.toystore.util.LoginAuditMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginAuditServiceImpl implements LoginAuditService {

    private final LoginAuditRepository loginAuditRepository;

    @Override
    public List<LoginAuditDto> findAll() {
        return loginAuditRepository.findAll().stream()
                .map(LoginAuditMapper::toDto)
                .toList();
    }
}
