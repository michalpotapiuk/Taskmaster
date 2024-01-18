package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.DTO.Node.NodeDTOBasicData;
import com.example.engineer.Model.NodeEntity;

import java.util.HashSet;
import java.util.Set;


public class NodeMapper {
    public static NodeDTO mapToDto(NodeEntity node) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(node.getId());
        nodeDTO.setTitle(node.getTitle());
        nodeDTO.setContent(node.getContent());
        nodeDTO.setPosX(node.getPosX());
        nodeDTO.setPosY(node.getPosY());

        if (node.getParentNode() != null) {
            nodeDTO.setParentNodeDTO(mapToDto(node.getParentNode()));
        }

        if (node.getDiagram() != null) {
            nodeDTO.setDiagramDTO(DiagramMapper.diagramDtoWithoutRelations(node.getDiagram()));
        }

        Set<NodeDTO> subNodesDTO = new HashSet<>();
        if (node.getSubNodes() != null) {
            for (NodeEntity subNode : node.getSubNodes()) {
                subNodesDTO.add(nodeDtoWithoutRelations(subNode));
            }
        }
        nodeDTO.setSubNodesDTO(subNodesDTO);

        return nodeDTO;
    }

    public static NodeEntity mapToEntity(NodeDTO nodeDTO) {
        NodeEntity node = new NodeEntity();
        node.setId(nodeDTO.getId());
        node.setTitle(nodeDTO.getTitle());
        node.setContent(nodeDTO.getContent());
        node.setPosX(nodeDTO.getPosX());
        node.setPosY(nodeDTO.getPosY());

        if (nodeDTO.getParentNodeDTO() != null) {
            node.setParentNode(mapToEntity(nodeDTO.getParentNodeDTO()));
        }

        if (nodeDTO.getDiagramDTO() != null) {
            node.setDiagram(DiagramMapper.mapToEntity(nodeDTO.getDiagramDTO()));
        }

        if (nodeDTO.getSubNodesDTO() != null) {
            Set<NodeEntity> subNodes = new HashSet<>();
            for (NodeDTO subNodeDTO : nodeDTO.getSubNodesDTO()) {
                subNodes.add(mapToEntity(subNodeDTO));
            }
            node.setSubNodes(subNodes);
        }
        return node;
    }


    public static NodeDTO nodeDtoWithoutRelations(NodeEntity nodeEntity) {
        if (nodeEntity == null) {
            return NodeDTO.builder().build();
        }
        return NodeDTO.builder()
                .id(nodeEntity.getId())
                .title(nodeEntity.getTitle())
                .content(nodeEntity.getContent())
                .posX(nodeEntity.getPosX())
                .posY(nodeEntity.getPosY())
                .build();
    }

    public static NodeDTOBasicData mapToNodeDtoBasicData(NodeEntity node) {
        return NodeDTOBasicData.builder()
                .id(node.getId())
                .title(node.getTitle())
                .content(node.getContent())
                .posX(node.getPosX())
                .posY(node.getPosY())
                .build();
    }
}
