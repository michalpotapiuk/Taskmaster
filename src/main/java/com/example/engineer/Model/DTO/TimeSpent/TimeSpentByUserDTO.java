package com.example.engineer.Model.DTO.TimeSpent;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class TimeSpentByUserDTO {
    UserDTO user;
    Map<TaskDTO, Double> timeSpent;
}
