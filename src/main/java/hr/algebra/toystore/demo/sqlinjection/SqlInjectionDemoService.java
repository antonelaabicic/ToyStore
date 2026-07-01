package hr.algebra.toystore.demo.sqlinjection;

import hr.algebra.toystore.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SqlInjectionDemoService {

    private final JdbcTemplate jdbcTemplate;

    public List<UserDto> searchUsersVulnerable(String username) {
        String sql = "SELECT username, name, surname, email, country, city, address FROM USERS " +
                "WHERE username = '" + username + "'";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class));
    }

    public List<UserDto> searchUsersSecure(String username) {
        String sql = "SELECT username, name, surname, email, country, city, address FROM USERS WHERE username = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class), username);
    }
}