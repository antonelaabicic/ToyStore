package hr.algebra.toystore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRegisterDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private String email;
    private String country;
    private String city;
    private String address;
}
