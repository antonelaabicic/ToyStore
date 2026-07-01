package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.dto.UserRegisterDto;
import hr.algebra.toystore.model.ApplicationRole;
import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.repository.ApplicationRoleRepository;
import hr.algebra.toystore.repository.ApplicationUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest {
    @Mock
    private ApplicationUserRepository userRepository;

    @Mock
    private ApplicationRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ApplicationUserServiceImpl applicationUserService;

    @Test
    @DisplayName("registerNewUser -> saves a new user")
    void registerNewUserSuccess() {
        UserRegisterDto dto = new UserRegisterDto("nninic", "123", "123",
                "Nina", "Ninic", "nina.ninic@gmail.com", "Hrvatska", "Osijek",
                "Ulica Zumbula 98a");

        ApplicationRole role = new ApplicationRole("ROLE_USER");

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(null);
        when(roleRepository.findByName(role.getName())).thenReturn(role);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("ENCODED_PASSWORD");

        applicationUserService.registerNewUser(dto);

        ArgumentCaptor<ApplicationUser> captor = ArgumentCaptor.forClass(ApplicationUser.class);
        verify(userRepository).save(captor.capture());
        ApplicationUser saved = captor.getValue();

        assertEquals("nninic", saved.getUsername());
        assertEquals("ENCODED_PASSWORD", saved.getPassword());
        assertEquals("Nina", saved.getName());
        assertEquals("Ninic", saved.getSurname());
        assertEquals("nina.ninic@gmail.com", saved.getEmail());
        assertEquals("Hrvatska", saved.getCountry());
        assertEquals("Osijek", saved.getCity());
        assertEquals("Ulica Zumbula 98a", saved.getAddress());
        assertEquals("ROLE_USER", saved.getRoles().get(0).getName());
    }

    @Test
    @DisplayName("registerNewUser -> throws when username already exists")
    void registerNewUserUsernameAlreadyExists() {
        UserRegisterDto dto = new UserRegisterDto("nninic", "123", "123",
                "Nina", "Ninic", "nina.ninic@gmail.com", "Hrvatska", "Osijek",
                "Ulica Zumbula 98a");

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(new ApplicationUser());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> applicationUserService.registerNewUser(dto)
        );

        assertEquals("Username already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("registerNewUser -> throws when default role does not exist")
    void registerNewUserRoleMissing() {
        UserRegisterDto dto = new UserRegisterDto("nninic", "123", "123",
                "Nina", "Ninic", "nina.ninic@gmail.com", "Hrvatska", "Osijek",
                "Ulica Zumbula 98a");

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(null);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> applicationUserService.registerNewUser(dto)
        );

        assertEquals("Default role not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("findByUsername -> returns UserDto")
    void findByUsernameSuccess() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");
        ApplicationUser user = ApplicationUser.builder()
                .username("nninic")
                .password("123")
                .name("Nina")
                .surname("Ninic")
                .email("nina@gmail.com")
                .country("Croatia")
                .city("Osijek")
                .address("Street")
                .roles(List.of(role))
                .build();

        when(userRepository.findByUsername("nninic")).thenReturn(user);

        UserDto dto = applicationUserService.findByUsername("nninic");

        assertEquals("nninic", dto.getUsername());
        assertEquals("Nina", dto.getName());
        assertEquals("Ninic", dto.getSurname());
        assertEquals("nina@gmail.com", dto.getEmail());
    }

    @Test
    @DisplayName("findByUsername -> throws when user is missing")
    void findByUsernameNotFound() {
        when(userRepository.findByUsername("nninic")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> applicationUserService.findByUsername("nninic")
        );

        assertEquals("User not found: nninic", exception.getMessage());
    }
}
