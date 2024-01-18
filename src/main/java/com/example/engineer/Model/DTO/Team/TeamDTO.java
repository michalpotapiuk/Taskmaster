package com.example.engineer.Model.DTO.Team;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    Long id;
    String name;
    Set<ProjectDTO> projects;
    Set<UserDTO> users;
}
