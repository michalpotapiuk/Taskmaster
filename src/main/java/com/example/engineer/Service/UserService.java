package com.example.engineer.Service;

import com.example.engineer.Model.DTO.User.UserCredentialsDto;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.DTO.User.UserRegistrationDto;
import com.example.engineer.Model.Mappers.UserMapper.UserCredentialsMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Model.User.UserRoleEntity;
import com.example.engineer.Repository.UserRepository;
import com.example.engineer.Repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private static final String USER_ROLE = "USER";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::mapToDto);
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository
                .findById(id)
                .map(UserMapper::mapToDto);
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsMapper::map);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void register(UserRegistrationDto registration) {
        UserEntity user = new UserEntity();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        String passwordHash = passwordEncoder
                .encode(registration.getPassword());
        user.setPassword(passwordHash);

        UserRoleEntity roles = userRoleRepository
                .findByName(USER_ROLE)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + USER_ROLE));

        user.setRoles(Set.of(roles));
        userRepository.save(user);
    }


}
