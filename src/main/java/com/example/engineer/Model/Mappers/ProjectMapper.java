package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.NoteEntity;
import com.example.engineer.Model.ProjectEntity;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.TeamEntity;
import com.example.engineer.Model.User.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ProjectMapper {
    public static ProjectDTO mapToDto(ProjectEntity project) {

        if (project == null) {
            return ProjectDTO.builder().build();
        }
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .diagramDTO(project.getDiagram() != null
                        ? DiagramMapper.mapToDto(project.getDiagram())
                        : null)
                .teamsDto(project.getTeams() != null
                        ? project.getTeams().stream()
                        .map(TeamMapper::teamDtoWithoutRelations)
                        .collect(Collectors.toSet())
                        : Collections.emptySet())
                .peopleDto(project.getUsers() != null
                        ? project.getUsers().stream()
                        .map(UserMapper::mapToDto)
                        .collect(Collectors.toSet())
                        : Collections.emptySet())
                .tasksDto(project.getTasks() != null
                        ? project.getTasks().stream()
                        .map(TaskMapper::mapToDto)
                        .collect(Collectors.toSet())
                        : Collections.emptySet())
                .notesDto(project.getNotes() != null
                        ? project.getNotes().stream()
                        .map(NoteMapper::mapToDto)
                        .collect(Collectors.toSet())
                        : Collections.emptySet())
                .build();
    }

    public static ProjectEntity mapToEntity(ProjectDTO projectDTO) {
        ProjectEntity project = new ProjectEntity();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        if (projectDTO.getDiagramDTO() != null) {
            project.setDiagram(DiagramMapper.mapToEntity(projectDTO.getDiagramDTO()));
        }

        Set<TeamEntity> teams = projectDTO.getTeamsDto() != null
                ? projectDTO.getTeamsDto().stream()
                .map(TeamMapper::mapToEntity)
                .collect(Collectors.toSet())
                : Collections.emptySet();
        project.setTeams(teams);

        Set<UserEntity> users = projectDTO.getPeopleDto() != null
                ? projectDTO.getPeopleDto().stream()
                .map(UserMapper::mapToEntity)
                .collect(Collectors.toSet())
                : Collections.emptySet();
        project.setUsers(users);

        Set<TaskEntity> tasks = projectDTO.getTasksDto() != null
                ? projectDTO.getTasksDto().stream()
                .map(TaskMapper::mapToEntity)
                .collect(Collectors.toSet())
                : Collections.emptySet();
        project.setTasks(tasks);

        Set<NoteEntity> notes = projectDTO.getNotesDto() != null
                ? projectDTO.getNotesDto().stream()
                .map(NoteMapper::mapToEntity)
                .collect(Collectors.toSet())
                : Collections.emptySet();
        project.setNotes(notes);

        return project;
    }

    public static ProjectDTO projectDtoWithoutRelations(ProjectEntity projectEntity) {
        return projectEntity == null ? null : ProjectDTO.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .build();
    }

    public static Set<ProjectDTO> projectDtoList(Set<ProjectEntity> projects) {
        Set<ProjectDTO> projectSet = new HashSet<>();

        for (ProjectEntity project : projects)
            projectSet.add(projectDtoWithoutRelations(project));

        return projectSet;
    }
}

