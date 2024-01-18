package com.example.engineer.Model.DTO.Diagram;

import com.example.engineer.Model.DTO.Node.NodeDTOBasicData;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagramDTONodes {
    Long id;
    String description;
    Set<NodeDTOBasicData> nodes;
}
