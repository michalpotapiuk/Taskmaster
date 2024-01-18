package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.TimeSpentEntity;


public class TimeSpentMapper {

    public static TimeSpentDTO map(TimeSpentEntity timeSpent) {
        TimeSpentDTO timeSpentDTO = new TimeSpentDTO();
        timeSpentDTO.setTime(timeSpent.getTime());

        if (timeSpent.getTask() != null) {
            timeSpentDTO.setTask(TaskMapper.mapToDto(timeSpent.getTask()));
        }

        if (timeSpent.getUser() != null) {
            timeSpentDTO.setUser(UserMapper.mapToDto(timeSpent.getUser()));
        }

        return timeSpentDTO;
    }

    public static TimeSpentEntity map(TimeSpentDTO timeSpentDTO) {
        TimeSpentEntity timeSpent = new TimeSpentEntity();
        timeSpent.setTime(timeSpentDTO.getTime());

        if (timeSpentDTO.getTask() != null) {
            timeSpent.setTask(TaskMapper.mapToEntity(timeSpentDTO.getTask()));
        }

        if (timeSpentDTO.getUser() != null) {
            timeSpent.setUser(UserMapper.mapToEntity(timeSpentDTO.getUser()));
        }

        return timeSpent;
    }
}
