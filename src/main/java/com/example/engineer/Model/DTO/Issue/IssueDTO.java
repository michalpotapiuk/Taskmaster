package com.example.engineer.Model.DTO.Issue;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {
    Long id;
    String name;
    String description;
    TaskDTO task;
}
