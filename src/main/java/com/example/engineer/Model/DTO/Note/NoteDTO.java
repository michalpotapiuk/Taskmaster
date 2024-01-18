package com.example.engineer.Model.DTO.Note;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {
    Long id;
    String note;
    ProjectDTO project;
    TaskDTO task;
    UserDTO user;
    DiagramDTO diagram;
}
