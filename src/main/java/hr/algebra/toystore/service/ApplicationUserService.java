package hr.algebra.toystore.service;

import hr.algebra.toystore.dto.UserDto;
import hr.algebra.toystore.dto.UserRegisterDto;
import hr.algebra.toystore.model.ApplicationUser;

public interface ApplicationUserService {
    void registerNewUser(UserRegisterDto dto);
    UserDto findByUsername(String username);
    ApplicationUser findUserByUsername(String username);
}
