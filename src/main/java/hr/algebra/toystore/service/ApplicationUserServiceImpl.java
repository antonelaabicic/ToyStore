package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.dto.UserRegisterDto;
import hr.algebra.toystore.model.ApplicationRole;
import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.repository.ApplicationRoleRepository;
import hr.algebra.toystore.repository.ApplicationUserRepository;
import hr.algebra.toystore.util.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class ApplicationUserServiceImpl implements ApplicationUserService {
    private final ApplicationUserRepository userRepository;
    private final ApplicationRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(UserRegisterDto dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        ApplicationRole userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            throw new RuntimeException("Default role not found");
        }

        ApplicationUser user = ApplicationUser.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .country(dto.getCountry())
                .city(dto.getCity())
                .address(dto.getAddress())
                .roles(Collections.singletonList(userRole))
                .build();

        userRepository.save(user);
    }

    public UserDto findByUsername(String username) {
        ApplicationUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return UserMapper.toDto(user);
    }

    @Override
    public ApplicationUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
