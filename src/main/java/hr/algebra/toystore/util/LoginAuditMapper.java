package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.LoginAuditDto;
import hr.algebra.toystore.model.LoginAudit;

public class LoginAuditMapper {
    private LoginAuditMapper() {
        throw new UnsupportedOperationException("LoginAuditMapper is a utility class.");
    }

    public static LoginAuditDto toDto(LoginAudit entity) {
        return new LoginAuditDto(
                entity.getUsername(),
                entity.getIpAddress(),
                entity.getTimestamp()
        );
    }

    public static LoginAudit toEntity(LoginAuditDto dto) {
        return LoginAudit.builder()
                .username(dto.getUsername())
                .ipAddress(dto.getIpAddress())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
