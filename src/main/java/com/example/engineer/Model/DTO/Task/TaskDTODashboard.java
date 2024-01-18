package com.example.engineer.Model.DTO.Task;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTODashboard {
    Long id;
    String name;
    String description;
    String projectName;
}
