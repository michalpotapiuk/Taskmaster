package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Diagram.DiagramDTONodes;
import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.DiagramEntity;
import com.example.engineer.Model.Mappers.DiagramMapper;
import com.example.engineer.Model.Mappers.NodeMapper;
import com.example.engineer.Model.NodeEntity;
import com.example.engineer.Repository.DiagramRepository;
import com.example.engineer.Repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiagramService {

    private final DiagramRepository diagramRepository;
    private final NodeRepository nodeRepository;


    public DiagramDTO create(DiagramDTO diagramDTO) {
        DiagramEntity mappedDiagram = DiagramMapper.mapToEntity(diagramDTO);
        DiagramEntity savedDiagram = diagramRepository.save(mappedDiagram);
        return DiagramMapper.mapToDto(savedDiagram);
    }

    @Transactional
    public Optional<DiagramDTO> findById(Long id) {
        return diagramRepository
                .findById(id)
                .map(DiagramMapper::mapToDto);
    }

    @Transactional
    public Optional<DiagramDTO> findByTitle(String title) {
        return diagramRepository
                .findDiagramByTitle(title)
                .map(DiagramMapper::mapToDto);
    }

    @Transactional
    public Optional<DiagramDTO> findByDescription(String description) {
        return diagramRepository
                .findDiagramByDescription(description)
                .map(DiagramMapper::mapToDto);
    }

    @Transactional
    public void delete(DiagramDTO diagramDTO) {
        DiagramEntity diagram = DiagramMapper.mapToEntity(diagramDTO);
        diagramRepository.save(diagram);
        diagramRepository.delete(diagram);
    }

    public void update(Long diagramId, DiagramDTO diagramDTO) {
        diagramDTO.setId(diagramId);
        DiagramEntity updateDiagram = DiagramMapper.mapToEntity(diagramDTO);
        diagramRepository.save(updateDiagram);
    }


    public void addNodeToDiagram(NodeDTO nodeDTO, DiagramDTO diagramDTO) {
        NodeEntity node = NodeMapper.mapToEntity(nodeDTO);
        DiagramEntity diagram = DiagramMapper.mapToEntity(diagramDTO);
        node.setDiagram(diagram);
        nodeRepository.save(node);
    }

    public Set<DiagramDTONodes> findDiagramWithNodes(DiagramDTO diagramDTO) {
        Set<DiagramDTONodes> diagramDTONodesSet = new HashSet<>();
        Set<DiagramEntity> diagramEntitySet = diagramRepository.findDiagramEntitiesWithNodesById(diagramDTO.getId());

        for (DiagramEntity diagram : diagramEntitySet) {
            diagramDTONodesSet.add(DiagramMapper.mapToDiagramDTOWithNodes(diagram));
        }

        return diagramDTONodesSet;

    }
}
