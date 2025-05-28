package hr.algebra.toystore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String country;
    private String city;
    private String address;
}
