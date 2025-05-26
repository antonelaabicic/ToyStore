package hr.algebra.toystore.listener;

import hr.algebra.toystore.model.LoginAudit;
import hr.algebra.toystore.repository.LoginAuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LoginAuditListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final LoginAuditRepository auditRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String ipAddress = request.getRemoteAddr();

        LoginAudit log = LoginAudit.builder()
                .username(username)
                .timestamp(LocalDateTime.now())
                .ipAddress(ipAddress)
                .build();

        auditRepository.save(log);
    }
}
