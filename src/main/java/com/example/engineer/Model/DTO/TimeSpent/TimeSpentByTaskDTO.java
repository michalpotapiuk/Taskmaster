package com.example.engineer.Model.DTO.TimeSpent;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class TimeSpentByTaskDTO {
    TaskDTO task;
    Map<UserDTO, Double> timeSpent;
}
