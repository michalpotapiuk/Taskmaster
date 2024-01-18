package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Diagram.DiagramDTONodes;
import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.DTO.Node.NodeDTOBasicData;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DiagramEntity;
import com.example.engineer.Model.NodeEntity;
import com.example.engineer.Model.NoteEntity;
import com.example.engineer.Model.ProjectEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class DiagramMapper {

    public static DiagramEntity mapToEntity(DiagramDTO diagramDTO) {
        DiagramEntity diagram = new DiagramEntity();
        diagram.setId(diagramDTO.getId());
        diagram.setTitle(diagramDTO.getTitle());
        diagram.setDescription(diagramDTO.getDescription());
        if (diagramDTO.getNotesDto() != null) {
            Set<NoteEntity> notes = diagramDTO.getNotesDto().stream()
                    .map(NoteMapper::mapToEntity)
                    .collect(Collectors.toSet());
            diagram.setNotes(notes);
        }
        if (diagramDTO.getNodesDto() != null) {
            Set<NodeEntity> nodes = diagramDTO.getNodesDto().stream()
                    .map(NodeMapper::mapToEntity)
                    .collect(Collectors.toSet());
            diagram.setNodes(nodes);
        }

        if (diagramDTO.getProjectsDto() != null) {
            Set<ProjectEntity> projects = diagramDTO.getProjectsDto().stream()
                    .map(ProjectMapper::mapToEntity)
                    .collect(Collectors.toSet());
            diagram.setProjects(projects);
        }

        return diagram;
    }

    public static DiagramDTO mapToDto(DiagramEntity diagram) {
        DiagramDTO diagramDTO = new DiagramDTO();
        diagramDTO.setId(diagram.getId());
        diagramDTO.setTitle(diagram.getTitle());
        diagramDTO.setDescription(diagram.getDescription());

        if (diagram.getNotes() != null) {
            Set<NoteDTO> notes = diagram.getNotes().stream()
                    .map(NoteMapper::mapToDto)
                    .collect(Collectors.toSet());
            diagramDTO.setNotesDto(notes);
        }

        if (diagram.getNodes() != null) {
            Set<NodeDTO> nodes = diagram.getNodes().stream()
                    .map(NodeMapper::nodeDtoWithoutRelations)
                    .collect(Collectors.toSet());
            diagramDTO.setNodesDto(nodes);
        }
        if (diagram.getProjects() != null) {
            Set<ProjectDTO> projects = diagram.getProjects().stream()
                    .map(ProjectMapper::mapToDto)
                    .collect(Collectors.toSet());
            diagramDTO.setProjectsDto(projects);
        }
        return diagramDTO;
    }

    public static DiagramDTO diagramDtoWithoutRelations(DiagramEntity diagramEntity) {
        return DiagramDTO.builder()
                .id(diagramEntity.getId())
                .title(diagramEntity.getTitle())
                .description(diagramEntity.getDescription())
                .build();
    }

    public static DiagramDTONodes mapToDiagramDTOWithNodes(DiagramEntity diagramEntity) {
        Set<NodeDTOBasicData> nodes = diagramEntity.getNodes() != null ?
                diagramEntity.getNodes().stream()
                        .map(NodeMapper::mapToNodeDtoBasicData)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        return DiagramDTONodes.builder()
                .id(diagramEntity.getId())
                .description(diagramEntity.getDescription())
                .nodes(nodes)
                .build();
    }

}
