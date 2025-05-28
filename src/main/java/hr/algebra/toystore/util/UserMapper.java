package hr.algebra.toystore.util;

import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.model.ApplicationUser;

public class UserMapper {
    private UserMapper() {
        throw new UnsupportedOperationException("UserMapper is a utility class.");
    }

    public static ApplicationUser toEntity(UserDto dto) {
        return ApplicationUser.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .country(dto.getCountry())
                .city(dto.getCity())
                .address(dto.getAddress())
                .build();
    }

    public static UserDto toDto(ApplicationUser user) {
        return new UserDto(
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getCountry(),
                user.getCity(),
                user.getAddress()
        );
    }
}
