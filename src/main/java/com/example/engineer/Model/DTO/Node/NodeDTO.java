package com.example.engineer.Model.DTO.Node;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO {
    Long id;
    String title;
    String content;
    Double posX;
    Double posY;
    DiagramDTO diagramDTO;
    NodeDTO parentNodeDTO;
    Set<NodeDTO> subNodesDTO;
}
