package com.example.engineer.Model.DTO.User;


import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Team.TeamDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String title;
    private Set<TeamDTO> teams;
    private Set<TimeSpentDTO> timeSpents;
    private Set<TaskDTO> tasks;
    private Set<ProjectDTO> projects;
    private Set<NoteDTO> notes;
}
