package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.Node.NodeDTO;
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
@RequestMapping("/nodes")
public class NodeController {

    private final NodeService nodeService;

    @PostMapping("/create")
    public ResponseEntity<String> createNode(@RequestBody String nodeJson) {
        return Optional.ofNullable(new Gson().fromJson(nodeJson, NodeDTO.class))
                .map(nodeDTO -> {
                    nodeService.create(nodeDTO);
                    return ResponseEntity.status(HttpStatus.CREATED).body("created");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return nodeService.findById(id)
                .map(nodeDTO -> {
                    nodeService.delete(nodeDTO);
                    return ResponseEntity.ok(String.format("Node with id: %s deleted", id));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Node with id: %snot found", id)));
    }

    @PostMapping("/{childId}/setParent/{parentId}")
    public ResponseEntity<String> setParent(@PathVariable Long childId, @PathVariable Long parentId) {
        if (nodeService.findById(childId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Child Node not found with id: %s", childId));
        }

        if (nodeService.findById(parentId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Child Node not found with id: %s", parentId));
        }

        NodeDTO childNodeDto = nodeService.findById(childId).get();
        NodeDTO parentNodeDto = nodeService.findById(parentId).get();

        if (childNodeDto.getParentNodeDTO() == null) {
            try {
                nodeService.addParent(childNodeDto, parentNodeDto);
                return ResponseEntity.ok("Parent Added to node");
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad");
    }
}
