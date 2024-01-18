package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.ProjectMapper;
import com.example.engineer.Service.*;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final DiagramService diagramService;
    private final TeamService teamService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody String projectJson) {
        return Optional.ofNullable(new Gson().fromJson(projectJson, ProjectDTO.class))
                .map(projectDTO -> {
                    projectService.create(projectDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(projectDTO -> ResponseEntity.ok(new Gson().toJson(ProjectMapper.mapToEntity(projectDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Project with id: %s not found", id)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return projectService.findById(id)
                .map(projectDTO -> {
                    projectService.delete(projectDTO);
                    return ResponseEntity.ok(String.format("Project with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Project with id: %snot found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String projectDTO) {
        return Optional.ofNullable(new Gson().fromJson(projectDTO, ProjectDTO.class))
                .map(issue -> {
                    projectService.update(id, issue);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Project with id: %s not found", id)));
    }

    @PostMapping("/{projectId}/addTeam/{teamId}")
    public ResponseEntity<String> addTeamToProject(
            @PathVariable Long projectId,
            @PathVariable Long teamId
    ) {
        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + projectId);
        }

        if (teamService.findById(teamId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found with id: " + teamId);
        }

        ProjectDTO projectDto = projectService.findById(projectId).get();
        TeamDTO teamDto = teamService.findById(teamId).get();

        try {
            projectService.addTeamToProject(projectDto, teamDto);
            return ResponseEntity.ok("Team added to project");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/admin/{projectId}/addUser/{userId}")
    public ResponseEntity<String> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + projectId);
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + projectId);
        }

        ProjectDTO projectDto = projectService.findById(projectId).get();
        UserDTO userDto = userService.findById(userId).get();

        try {
            projectService.addUserToProject(userDto, projectDto);
            return ResponseEntity.ok("User added to project");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/admin/{projectId}/deleteUser/{userId}")
    public ResponseEntity<String> deleteUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + projectId);
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + projectId);
        }
        ProjectDTO projectDto = projectService.findById(projectId).get();
        UserDTO userDto = userService.findById(userId).get();

        try {
            projectService.deleteUserFromProject(userDto, projectDto);
            return ResponseEntity.ok("User deleted from project");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{projectId}/addTask/{taskId}")
    public ResponseEntity<String> addTaskToProject(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + projectId);
        }

        if (taskService.findById(taskId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with id: " + taskId);
        }
        ProjectDTO projectDto = projectService.findById(projectId).get();
        TaskDTO taskDTO = taskService.findById(taskId).get();

        try {
            projectService.addTaskToProject(projectDto, taskDTO);
            return ResponseEntity.ok("Task added to project");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/{projectId}/addDiagram/{diagramId}")
    public ResponseEntity<String> addDiagramToProject(@PathVariable Long projectId, @PathVariable Long diagramId) {
        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note with id " + projectId + " not found");
        }

        if (diagramService.findById(diagramId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Diagram with id " + diagramId + " not found");
        }

        ProjectDTO projectDTO = projectService.findById(projectId).get();
        DiagramDTO diagramDTO = diagramService.findById(diagramId).get();

        try {
            projectService.addDiagramToProject(projectDTO, diagramDTO);
            return ResponseEntity.ok("Note added to Diagram");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
