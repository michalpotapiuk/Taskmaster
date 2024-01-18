package com.example.engineer.Model.DTO.Project;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    Long id;
    String name;
    DiagramDTO diagramDTO;
    Set<TeamDTO> teamsDto;
    Set<UserDTO> peopleDto;
    Set<TaskDTO> tasksDto;
    Set<NoteDTO> notesDto;
}
