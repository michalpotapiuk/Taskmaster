package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Node.NodeDTO;
import com.example.engineer.Model.Mappers.DiagramMapper;
import com.example.engineer.Service.DiagramService;
import com.example.engineer.Service.NodeService;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagrams")
public class DiagramController {

    private final DiagramService diagramService;
    private final NodeService nodeService;

    @PostMapping("/create")
    public ResponseEntity<String> createDiagram(@RequestBody String diagramJson) {
        return Optional.ofNullable(new Gson().fromJson(diagramJson, DiagramDTO.class))
                .map(diagramDTO -> {
                    diagramService.create(diagramDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id) {
        return diagramService.findById(id)
                .map(diagramDTO -> ResponseEntity.ok(new Gson().toJson(DiagramMapper.mapToEntity(diagramDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Diagram with id: %s not found", id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return diagramService.findById(id)
                .map(diagram -> {
                    diagramService.delete(diagram);
                    return ResponseEntity.ok(String.format("Diagram with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Diagram with id: %s not found", id)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody String diagramDTO) {
        return Optional.ofNullable(new Gson().fromJson(diagramDTO, DiagramDTO.class))
                .map(diagram -> {
                    diagramService.update(id, diagram);
                    return ResponseEntity.ok("Updated successfully");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Diagram with id: %s not found", id)));
    }


    @PostMapping("/{diagramId}/addNode/{nodeId}")
    public ResponseEntity<String> addNodeToDiagram(
            @PathVariable Long diagramId,
            @PathVariable Long nodeId
    ) {
        if (diagramService.findById(diagramId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Diagram not found with id: " + diagramId);
        }

        if (nodeService.findById(nodeId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Node not found with id: " + nodeId);
        }

        DiagramDTO diagramDTO = diagramService.findById(diagramId).get();
        NodeDTO nodeDTO = nodeService.findById(nodeId).get();

        try {
            diagramService.addNodeToDiagram(nodeDTO, diagramDTO);
            return ResponseEntity.ok("Node added to Diagram");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/diagram-nodes/{diagramId}")
    public ResponseEntity<String> findDiagramWithNodes(@PathVariable Long diagramId) {
        return diagramService.findById(diagramId)
                .map(diagramDTO -> ResponseEntity.ok(new Gson().toJson(diagramService.findDiagramWithNodes(diagramDTO))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Diagram with id: %s not found", diagramId)));
    }
}

