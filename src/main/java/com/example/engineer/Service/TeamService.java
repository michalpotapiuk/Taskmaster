package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.Team.TeamDTOMembers;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.ProjectMapper;
import com.example.engineer.Model.Mappers.TeamMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.TeamEntity;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Repository.ProjectRepository;
import com.example.engineer.Repository.TeamRepository;
import com.example.engineer.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TeamDTO create(TeamDTO teamDto) {
        TeamEntity team = TeamMapper.mapToEntity(teamDto);
        TeamEntity savedTeam = teamRepository.save(team);
        return TeamMapper.mapToDto(savedTeam);
    }

    public Optional<TeamDTO> findById(Long id) {
        return teamRepository
                .findById(id)
                .map(TeamMapper::mapToDto);
    }

    public Set<TeamDTOMembers> findUserTeamWithMembers(UserDTO userDTO) {
        Set<TeamDTOMembers> teamMembersSet = new HashSet<>();
        Set<TeamEntity> teamSet = teamRepository.findTeamEntitiesByUser(userDTO.getId());

        for (TeamEntity team : teamSet) {
            teamMembersSet.add(TeamMapper.mapToTeamDTOWithMembers(team));
        }

        return teamMembersSet;
    }

    public boolean isNamePresent(TeamDTO teamDTO) {
        TeamEntity team = TeamMapper.mapToEntity(teamDTO);
        return teamRepository.findByName(team.getName()).isPresent();
    }

    @Transactional
    public void delete(TeamDTO teamDTO) {

        Set<ProjectDTO> projects = teamDTO.getProjects();
        if (!projects.isEmpty()) {
            for (ProjectDTO project : projects) {
                project.getTeamsDto().remove(teamDTO);
                projectRepository.save(ProjectMapper.mapToEntity(project));
            }
        }

        Set<UserDTO> users = teamDTO.getUsers();
        if (!users.isEmpty()) {
            for (UserDTO userDTO : users) {
                userDTO.getTeams().remove(teamDTO);
                userRepository.save(UserMapper.mapToEntity(userDTO));
            }
        }
        teamRepository.delete(TeamMapper.mapToEntity(teamDTO));
    }

    public void update(Long id, TeamDTO teamDTO) {
        teamDTO.setId(id);
        TeamEntity updateTeam = TeamMapper.mapToEntity(teamDTO);
        teamRepository.save(updateTeam);
    }

    public void addUserToTeam(UserDTO userDTO, TeamDTO teamDTO) {
        UserEntity user = UserMapper.mapToEntity(userDTO);
        TeamEntity team = TeamMapper.mapToEntity(teamDTO);
        user.getTeams().add(team);
        userRepository.save(user);
    }

    public void deleteUserFromTeam(UserDTO userDTO, TeamDTO teamDTO) {
        UserEntity user = UserMapper.mapToEntity(userDTO);
        TeamEntity team = TeamMapper.mapToEntity(teamDTO);

        team.getUsers().remove(user);
        teamRepository.save(team);
    }
}
