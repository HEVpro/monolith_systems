package org.shrtr.core.services;

import lombok.RequiredArgsConstructor;
import org.shrtr.core.controllers.AuthenticationController;
import org.shrtr.core.domain.entities.User;
import org.shrtr.core.domain.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User create(AuthenticationController.CreateUserRequest request) {
    if (usersRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new ValidationException("Username already exists");
    }
    if (!request.getPassword().equals(request.getRePassword())) {
      throw new ValidationException("Passwords do not match");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setMax_requests(5);
    user.setMax_requests_time_window_ms(600000);

    usersRepository.save(user);
    return user;
  }
}
