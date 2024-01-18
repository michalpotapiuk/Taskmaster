package com.example.engineer.Model.DTO.TimeSpent;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSpentDTO {
    Double time;
    TaskDTO task;
    UserDTO user;
}
