package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.Team.TeamDTOMembers;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.DTO.User.UserDTOBasicData;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.ProjectEntity;
import com.example.engineer.Model.TeamEntity;
import com.example.engineer.Model.User.UserEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


public class TeamMapper {

    public static TeamDTO mapToDto(TeamEntity team) {

        Set<ProjectDTO> projects = team.getProjects() != null ?
                team.getProjects().stream()
                        .map(ProjectMapper::mapToDto)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        Set<UserDTO> users = team.getUsers() != null ?
                team.getUsers().stream()
                        .map(UserMapper::userDtoWithoutRelations)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .projects(projects)
                .users(users)
                .build();
    }


    public static TeamEntity mapToEntity(TeamDTO teamDTO) {
        TeamEntity team = new TeamEntity();
        team.setId(teamDTO.getId());
        team.setName(teamDTO.getName());

        if (teamDTO.getProjects() != null) {
            Set<ProjectEntity> projects = teamDTO.getProjects().stream()
                    .map(ProjectMapper::mapToEntity)
                    .collect(Collectors.toSet());
            team.setProjects(projects);
        }

        if (teamDTO.getUsers() != null) {
            Set<UserEntity> users = teamDTO.getUsers().stream()
                    .map(UserMapper::mapToEntity)
                    .collect(Collectors.toSet());
            team.setUsers(users);
        }
        return team;
    }

    public static TeamDTO teamDtoWithoutRelations(TeamEntity teamEntity) {
        return teamEntity == null ? null : TeamDTO.builder()
                .id(teamEntity.getId())
                .name(teamEntity.getName())
                .build();
    }

    public static TeamDTOMembers mapToTeamDTOWithMembers(TeamEntity teamEntity) {
        Set<UserDTOBasicData> users = teamEntity.getUsers() != null ?
                teamEntity.getUsers().stream()
                        .map(UserMapper::mapToUserBasicData)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        return TeamDTOMembers.builder()
                .id(teamEntity.getId())
                .name(teamEntity.getName())
                .members(users)
                .build();

    }
}

