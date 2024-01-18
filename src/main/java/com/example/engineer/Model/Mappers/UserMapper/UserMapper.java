package com.example.engineer.Model.Mappers.UserMapper;

import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.DTO.User.UserDTOBasicData;
import com.example.engineer.Model.Mappers.*;
import com.example.engineer.Model.User.UserEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;


public class UserMapper {
    public static UserEntity mapToEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setId(userDTO.getId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setTitle(userDTO.getTitle());

        if (userDTO.getTeams() != null) {
            user.setTeams(userDTO.getTeams()
                    .stream().map(TeamMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (userDTO.getTimeSpents() != null) {
            user.setTimeSpents(userDTO.getTimeSpents()
                    .stream().map(TimeSpentMapper::map)
                    .collect(Collectors.toSet()));
        }

        if (userDTO.getTasks() != null) {
            user.setTasks(userDTO.getTasks()
                    .stream().map(TaskMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (userDTO.getProjects() != null) {
            user.setProjects(userDTO.getProjects()
                    .stream().map(ProjectMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        if (userDTO.getNotes() != null) {
            user.setNotes(userDTO.getNotes()
                    .stream().map(NoteMapper::mapToEntity)
                    .collect(Collectors.toSet()));
        }

        return user;
    }

    public static UserDTO mapToDto(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .title(user.getTitle())
                .teams(user.getTeams() != null ? user.getTeams().stream()
                        .map(TeamMapper::teamDtoWithoutRelations)
                        .collect(Collectors.toSet()) : Collections.emptySet())
                .timeSpents(user.getTimeSpents() != null ? user.getTimeSpents().stream()
                        .map(TimeSpentMapper::map)
                        .collect(Collectors.toCollection(HashSet::new)) : Collections.emptySet())
                .tasks(user.getTasks() != null ? user.getTasks().stream()
                        .map(TaskMapper::mapToDto)
                        .collect(Collectors.toSet()) : Collections.emptySet())
                .projects(user.getProjects() != null ? user.getProjects().stream()
                        .map(ProjectMapper::mapToDto)
                        .collect(Collectors.toSet()) : Collections.emptySet())
                .notes(user.getNotes() != null ? user.getNotes().stream()
                        .map(NoteMapper::mapToDto)
                        .collect(Collectors.toSet()) : Collections.emptySet())
                .build();
    }


    public static UserDTO userDtoWithoutRelations(UserEntity user) {
        return UserDTO.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .title(user.getTitle())
                .build();
    }

    public static UserDTOBasicData mapToUserBasicData(UserEntity user) {
        return UserDTOBasicData.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .title(user.getTitle())
                .build();
    }
}
