package com.example.engineer.Model.Mappers.UserMapper;

import com.example.engineer.Model.DTO.User.UserCredentialsDto;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Model.User.UserRoleEntity;

import java.util.Set;
import java.util.stream.Collectors;


public class UserCredentialsMapper {

    public static UserCredentialsDto map(UserEntity user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRoleEntity::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(email, password, roles);
    }
}
