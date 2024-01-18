package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Task.TaskDTODashboard;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Service.TaskService;
import com.example.engineer.Service.UserService;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody String taskJson) {
        return Optional.ofNullable(new Gson().fromJson(taskJson, TaskDTO.class))
                .map(taskDTO -> {
                    taskService.create(taskDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return taskService.findForGetEndpointById(id)
                .map(taskDTO -> ResponseEntity.ok(new Gson().toJson(TaskMapper.mapToEntity(taskDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Task with id: %s not found", id)));
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<String> findUserTasks(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(userDTO -> {
                    Set<TaskDTODashboard> userTasks = taskService.findUserTasks(userDTO);
                    if (!userTasks.isEmpty()) {
                        return ResponseEntity.ok(new Gson().toJson(userTasks));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(String.format("No tasks found for user with id: %s", userId));
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("User with id: %s not found", userId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return taskService.findById(id)
                .map(taskDTO -> {
                    taskService.delete(taskDTO);
                    return ResponseEntity.ok(String.format("Task with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Task with id: %snot found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String taskJson) {
        return Optional.ofNullable(new Gson().fromJson(taskJson, TaskDTO.class))
                .map(taskDTO -> {
                    taskService.update(id, taskDTO);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Task with id: %s not found", id)));
    }

    @GetMapping("/subtasks/{id}")
    public ResponseEntity<String> findSubtasks(@PathVariable Long id) {
        return taskService.findById(id)
                .map(taskDTO -> ResponseEntity.ok(new Gson().toJson(taskService.findSubtasks(taskDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Task with id: %s not found", id)));
    }

    @PostMapping("/{taskId}/setParent/{parentId}")
    public ResponseEntity<String> setParent(
            @PathVariable Long taskId,
            @PathVariable Long parentId
    ) {
        if (taskService.findById(taskId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with id: " + taskId);
        }

        if (taskService.findById(parentId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent not found with id: " + parentId);
        }

        TaskDTO taskDTO = taskService.findById(taskId).get();
        TaskDTO parentDTO = taskService.findById(parentId).get();

        if (taskDTO.getParentTask() == null) {//dorobić coś jak jest już subtaskiem
            try {
                taskService.setAsSubtask(taskDTO, parentDTO);
                return ResponseEntity.ok("Task updated");
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{taskId}/addUser/{userId}")
    public ResponseEntity<String> addTaskToUser(@PathVariable Long taskId, @PathVariable Long userId) {
        if (taskService.findById(taskId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with id: " + taskId);
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        }

        TaskDTO taskDTO = taskService.findById(taskId).get();
        UserDTO userDTO = userService.findById(userId).get();
        try {
            taskService.addTaskToUser(taskDTO, userDTO);
            return ResponseEntity.ok("Task added to user");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
    }
}
