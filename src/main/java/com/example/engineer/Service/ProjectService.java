package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.DiagramEntity;
import com.example.engineer.Model.Mappers.DiagramMapper;
import com.example.engineer.Model.Mappers.ProjectMapper;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.Mappers.TeamMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.ProjectEntity;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.TeamEntity;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Repository.ProjectRepository;
import com.example.engineer.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectDTO create(ProjectDTO projectDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        ProjectEntity savedProject = projectRepository.save(project);
        return ProjectMapper.mapToDto(savedProject);
    }

    public Optional<ProjectDTO> findById(Long id) {
        return projectRepository
                .findById(id)
                .map(ProjectMapper::mapToDto);
    }

    public boolean isNamePresent(ProjectDTO projectDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        return projectRepository.findByName(project.getName()) != null;
    }

    public void addTeamToProject(ProjectDTO projectDTO, TeamDTO teamDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        TeamEntity team = TeamMapper.mapToEntity(teamDTO);

        project.getTeams().add(team);
        projectRepository.save(project);
    }

    public void addUserToProject(UserDTO userDTO, ProjectDTO projectDTO) {
        UserEntity user = UserMapper.mapToEntity(userDTO);
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);

        project.getUsers().add(user);
        projectRepository.save(project);
    }

    public void deleteUserFromProject(UserDTO userDTO, ProjectDTO projectDTO) {
        UserEntity user = UserMapper.mapToEntity(userDTO);
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);

        project.getUsers().remove(user);
        projectRepository.save(project);
    }


    public void addTaskToProject(ProjectDTO projectDTO, TaskDTO taskDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);

        task.setProject(project);
        taskRepository.save(task);
    }

    @Transactional
    public void delete(ProjectDTO projectDTO) {
        ProjectEntity mappedProject = ProjectMapper.mapToEntity(projectDTO);

        //clear users
        mappedProject.setUsers(null);

        //clear teams
        mappedProject.setTeams(null);

        projectRepository.save(mappedProject);
        projectRepository.delete(mappedProject);
    }

    public void update(Long id, ProjectDTO projectDTO) {
        projectDTO.setId(id);
        ProjectEntity updateProject = ProjectMapper.mapToEntity(projectDTO);
        projectRepository.save(updateProject);
    }

    public void addDiagramToProject(ProjectDTO projectDTO, DiagramDTO diagramDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        DiagramEntity diagram = DiagramMapper.mapToEntity(diagramDTO);
        project.setDiagram(diagram);
        projectRepository.save(project);
    }
}
