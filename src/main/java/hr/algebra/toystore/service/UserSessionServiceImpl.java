package hr.algebra.toystore.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final HttpSession session;

    @Override
    public String getCurrentSessionId() {
        return session.getId();
    }
}
