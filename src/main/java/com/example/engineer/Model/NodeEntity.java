package com.example.engineer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "node")
public class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Double posX;
    private Double posY;
    @ManyToOne
    @JoinColumn(name = "diagram_id")
    private DiagramEntity diagram;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_node_id")
    private NodeEntity parentNode;

    @OneToMany(mappedBy = "parentNode", fetch = FetchType.EAGER)
    private Set<NodeEntity> subNodes;
}
