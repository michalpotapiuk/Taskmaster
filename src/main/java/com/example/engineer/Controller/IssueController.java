package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Issue.IssueDTO;
import com.example.engineer.Model.DTO.Issue.IssueDTODashboard;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.Mappers.IssueMapper;
import com.example.engineer.Service.IssueService;
import com.example.engineer.Service.TaskService;
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
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;
    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<String> createIssue(@RequestBody String issueJson) {
        return Optional.ofNullable(new Gson().fromJson(issueJson, IssueDTO.class))
                .map(issueDTO -> {
                    issueService.create(issueDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return issueService.findById(id)
                .map(issueDTO -> ResponseEntity.ok(new Gson().toJson(IssueMapper.mapToEntity(issueDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Issue with id: %s not found", id)));
    }

    @GetMapping("/dashboard/global-issues")
    public ResponseEntity<String> findAllGlobalIssues() {
        Set<IssueDTODashboard> issueDTODashboardSet = issueService.findAllGlobalIssue();
        if (issueDTODashboardSet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Not found any global issue");
        }
        return ResponseEntity.ok(new Gson().toJson(issueDTODashboardSet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return issueService.findById(id)
                .map(issueDTO -> {
                    issueService.delete(issueDTO);
                    return ResponseEntity.ok(String.format("Issue with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Issue with id: %snot found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String issueJson) {
        return Optional.ofNullable(new Gson().fromJson(issueJson, IssueDTO.class))
                .map(issue -> {
                    issueService.update(id, issue);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Issue with id: %s not found", id)));
    }

    @PostMapping("/{issueId}/addIssue/{taskId}")
    public ResponseEntity<String> addIssueToTask(
            @PathVariable Long issueId,
            @PathVariable Long taskId
    ) {
        if (issueService.findById(issueId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue with id " + issueId + " not found");
        }

        if (taskService.findById(taskId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id " + taskId + " not found");
        }

        IssueDTO issueDTO = issueService.findById(issueId).get();
        TaskDTO taskDTO = taskService.findById(taskId).get();

        try {
            issueService.addIssueToTask(issueDTO, taskDTO);
            return ResponseEntity.ok("Issue added to task");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
