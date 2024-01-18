package com.example.engineer.Model;

import com.example.engineer.Model.User.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "team")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
    private Set<ProjectEntity> projects;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
    private Set<UserEntity> users;

}
