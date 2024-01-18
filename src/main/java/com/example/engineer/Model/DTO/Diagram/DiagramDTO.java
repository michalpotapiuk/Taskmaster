package com.example.engineer.Model.DTO.Diagram;

import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagramDTO {
    Long id;
    String title;
    String description;
    Set<NoteDTO> notesDto;
    Set<NodeDTO> nodesDto;
    Set<ProjectDTO> projectsDto;
}
