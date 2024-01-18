package com.example.engineer.Model.DTO.Task;

import com.example.engineer.Model.DTO.Issue.IssueDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Report.ReportDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Enums.Priority;
import com.example.engineer.Model.Enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    Long id;
    String name;
    Status status;
    Priority priority;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    Set<UserDTO> persons;
    ProjectDTO project;
    Set<TimeSpentDTO> timeSpent;
    Set<IssueDTO> issues;
    ReportDTO report;
    Set<NoteDTO> notes;
    TaskDTO parentTask;
    Set<TaskDTO> subtasks;
}
