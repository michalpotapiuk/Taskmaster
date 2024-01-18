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
@Table(name = "diagram")
public class DiagramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.REMOVE)
    private Set<NoteEntity> notes;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.REMOVE)
    private Set<NodeEntity> nodes;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.REMOVE)
    private Set<ProjectEntity> projects;

}

