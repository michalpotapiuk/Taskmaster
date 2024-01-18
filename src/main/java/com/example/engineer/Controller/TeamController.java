package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.Team.TeamDTOMembers;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.TeamMapper;
import com.example.engineer.Service.ProjectService;
import com.example.engineer.Service.TeamService;
import com.example.engineer.Service.UserService;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final UserService userService;
    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody String teamJson) {
        TeamDTO teamDTO = new Gson().fromJson(teamJson, TeamDTO.class);
        return Stream.of(teamDTO)
                .filter(teamService::isNamePresent)
                .findFirst()
                .map(team -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.format("Team with name: %s already exists", team.getName())))
                .orElseGet(() -> {
                    teamService.create(teamDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return teamService.findById(id)
                .map(teamDTO -> ResponseEntity.ok(new Gson().toJson(TeamMapper.mapToEntity(teamDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Team with id: %s not found", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return teamService.findById(id)
                .map(teamDTO -> {
                    teamService.delete(teamDTO);
                    return ResponseEntity.ok(String.format("Team with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Team with id: %snot found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String teamJson) {
        return Optional.ofNullable(new Gson().fromJson(teamJson, TeamDTO.class))
                .map(teamDTO -> {
                    teamService.update(id, teamDTO);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Team with id: %s not found", id)));
    }

    @PostMapping("/{teamId}/addProject/{projectId}")
    public ResponseEntity<String> addTeamToProject(@PathVariable Long teamId, @PathVariable Long projectId) {
        if (teamService.findById(teamId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found with id: " + teamId);
        }

        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found with id: " + projectId);
        }

        TeamDTO teamDto = teamService.findById(teamId).get();
        ProjectDTO projectDTO = projectService.findById(projectId).get();

        try {
            projectService.addTeamToProject(projectDTO, teamDto);
            return ResponseEntity.ok("Project added to team");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/admin/{teamId}/addUser/{userId}")
    public ResponseEntity<String> addUserToTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        if (teamService.findById(teamId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found with id: " + teamId);
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        }

        TeamDTO teamDto = teamService.findById(teamId).get();
        UserDTO userDto = userService.findById(userId).get();

        try {
            teamService.addUserToTeam(userDto, teamDto);
            return ResponseEntity.ok("User added to team");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/admin/{teamId}/deleteUser/{userId}")
    public ResponseEntity<String> deleteUserFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        if (teamService.findById(teamId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found with id: " + teamId);
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        }

        TeamDTO teamDto = teamService.findById(teamId).get();
        UserDTO userDto = userService.findById(userId).get();

        try {
            teamService.deleteUserFromTeam(userDto, teamDto);
            return ResponseEntity.ok("Deleted user from team");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user-teams/{userId}")
    public ResponseEntity<String> findUserTeams(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(userDTO -> {
                    Set<TeamDTOMembers> teamDTOMembers = teamService.findUserTeamWithMembers(userDTO);
                    if (teamDTOMembers.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                .body("Not found any teams for user");
                    } else {
                        return ResponseEntity.ok(new Gson().toJson(teamDTOMembers));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("User with id: %s not found", userId)));
    }
}
