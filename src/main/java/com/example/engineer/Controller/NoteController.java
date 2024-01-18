package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.NoteMapper;
import com.example.engineer.Service.*;
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
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;
    private final DiagramService diagramService;

    @PostMapping("/create")
    public ResponseEntity<String> createDiagram(@RequestBody String noteJson) {
        return Optional.ofNullable(new Gson().fromJson(noteJson, NoteDTO.class))
                .map(noteDTO -> {
                    noteService.create(noteDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return noteService.findById(id)
                .map(noteDTO -> ResponseEntity.ok(new Gson().toJson(NoteMapper.mapToEntity(noteDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Note with id: %s not found", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return noteService.findById(id)
                .map(noteDTO -> {
                    noteService.delete(noteDTO);
                    return ResponseEntity.ok(String.format("Note with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Note with id: %snot found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String noteDTO) {
        return Optional.ofNullable(new Gson().fromJson(noteDTO, NoteDTO.class))
                .map(note -> {
                    noteService.update(id, note);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Note with id: %s not found", id)));
    }

    @PostMapping("/{noteId}/addProject/{projectId}")
    public ResponseEntity<String> addNoteToProject(@PathVariable Long noteId, @PathVariable Long projectId) {
        if (noteService.findById(noteId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note with id " + noteId + " not found");
        }

        if (projectService.findById(projectId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with id " + projectId + " not found");
        }

        NoteDTO noteDTO = noteService.findById(noteId).get();
        ProjectDTO projectDTO = projectService.findById(projectId).get();

        try {
            noteService.addNoteToProject(noteDTO, projectDTO);
            return ResponseEntity.ok("Note added to project");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{noteId}/addTask/{taskId}")
    public ResponseEntity<String> addNoteToTask(@PathVariable Long noteId, @PathVariable Long taskId) {
        if (noteService.findById(noteId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note with id " + noteId + " not found");
        }

        if (taskService.findById(taskId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id " + taskId + " not found");
        }

        NoteDTO noteDTO = noteService.findById(noteId).get();
        TaskDTO taskDTO = taskService.findById(taskId).get();

        try {
            noteService.addNoteToTask(noteDTO, taskDTO);
            return ResponseEntity.ok("Note added to Task");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/{noteId}/addUser/{userId}")
    public ResponseEntity<String> addNoteToUser(@PathVariable Long noteId, @PathVariable Long userId) {
        if (noteService.findById(noteId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note with id " + noteId + " not found");
        }

        if (userService.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + userId + " not found");
        }

        NoteDTO noteDTO = noteService.findById(noteId).get();
        UserDTO userDTO = userService.findById(userId).get();

        try {
            noteService.addNoteToUser(noteDTO, userDTO);
            return ResponseEntity.ok("Note added to User");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{noteId}/addDiagram/{diagramId}")
    public ResponseEntity<String> addNoteToDiagram(@PathVariable Long noteId, @PathVariable Long diagramId) {
        if (noteService.findById(noteId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note with id " + noteId + " not found");
        }

        if (diagramService.findById(diagramId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Diagram with id " + diagramId + " not found");
        }

        NoteDTO noteDTO = noteService.findById(noteId).get();
        DiagramDTO diagramDTO = diagramService.findById(diagramId).get();

        try {
            noteService.addNoteToDiagram(noteDTO, diagramDTO);
            return ResponseEntity.ok("Note added to Diagram");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/user-notes/{userId}")
    public ResponseEntity<String> findUserNotes(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(userDTO -> {
                    Set<NoteDTO> noteDTOSet = noteService.findUserNotes(userDTO);
                    if (noteDTOSet.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                .body("Not found any notes for user");
                    }
                    return ResponseEntity.ok(new Gson().toJson(noteDTOSet));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("User with id: %s not found", userId)));
    }
}
