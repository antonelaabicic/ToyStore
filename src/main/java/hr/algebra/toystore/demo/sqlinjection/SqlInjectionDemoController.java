package hr.algebra.toystore.demo.sqlinjection;

import hr.algebra.toystore.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demo/sql")
@RequiredArgsConstructor
public class SqlInjectionDemoController {

    private final SqlInjectionDemoService sqlInjectionDemoService;

    @GetMapping("/vulnerable/users")
    public List<UserDto> searchUsersVulnerable(@RequestParam String username) {
        return sqlInjectionDemoService.searchUsersVulnerable(username);
    }

    @GetMapping("/secure/users")
    public List<UserDto> searchUsersSecure(@RequestParam String username) {
        return sqlInjectionDemoService.searchUsersSecure(username);
    }
}
