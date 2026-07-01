package hr.algebra.toystore.service;

import hr.algebra.toystore.model.ApplicationRole;
import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.repository.ApplicationUserRepository;
import hr.algebra.toystore.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private ApplicationUserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("loadUserByUsername -> returns UserDetails for existing user")
    void loadUserByUsername() {
        ApplicationRole userRole = new ApplicationRole("ROLE_USER");
        ApplicationRole adminRole = new ApplicationRole("ROLE_ADMIN");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .password("password")
                .roles(List.of(userRole, adminRole))
                .build();

        when(userRepository.findByUsername("pperic")).thenReturn(user);

        UserDetails result = userDetailsService.loadUserByUsername("pperic");

        assertNotNull(result);
        assertEquals("pperic", result.getUsername());
        assertEquals("password", result.getPassword());

        assertEquals(2, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isEnabled());
        verify(userRepository).findByUsername("pperic");
    }

    @Test
    @DisplayName("loadUserByUsername -> throws when user does not exist")
    void loadUserByUsernameThrows() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );

        assertEquals("The user 'unknown' does not exist!", exception.getMessage());
        verify(userRepository).findByUsername("unknown");
    }
}