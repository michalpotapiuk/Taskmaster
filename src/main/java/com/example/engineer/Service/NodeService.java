package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.Mappers.NodeMapper;
import com.example.engineer.Model.NodeEntity;
import com.example.engineer.Repository.DiagramRepository;
import com.example.engineer.Repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository nodeRepository;
    private final DiagramRepository diagramRepository;

    public Optional<NodeDTO> findById(Long id) {
        return nodeRepository
                .findById(id)
                .map(NodeMapper::mapToDto);
    }

    public NodeDTO create(NodeDTO nodeDTO) {
        NodeEntity node = NodeMapper.mapToEntity(nodeDTO);
        NodeEntity savedNode = nodeRepository.save(node);
        return NodeMapper.mapToDto(savedNode);
    }

    @Transactional
    public void addParent(NodeDTO childNodeDto, NodeDTO parentNodeDto) {
        NodeEntity childNode = NodeMapper.mapToEntity(childNodeDto);
        NodeEntity parentNode = NodeMapper.mapToEntity(parentNodeDto);
        childNode.setParentNode(parentNode);
        nodeRepository.save(childNode);
    }

    @Transactional
    public void delete(NodeDTO nodeDTO) {
        //remove diagram info
        nodeDTO.setDiagramDTO(null);
        //remove parent
        nodeDTO.setParentNodeDTO(null);
        //remove from children
        Set<NodeDTO> subs = nodeDTO.getSubNodesDTO();
        if (!subs.isEmpty()) {
            for (NodeDTO sub : subs) {
                sub.setParentNodeDTO(null);
                nodeRepository.save(NodeMapper.mapToEntity(sub));
            }
        }
        nodeRepository.save(NodeMapper.mapToEntity(nodeDTO));
        nodeRepository.delete(NodeMapper.mapToEntity(nodeDTO));
    }
}
