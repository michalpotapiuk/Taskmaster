package com.example.engineer.Model.DTO.Report;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;
    private String content;
    private TaskDTO task;
}
