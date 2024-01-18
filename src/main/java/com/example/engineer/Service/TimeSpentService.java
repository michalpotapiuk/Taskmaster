package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentByTaskDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentByUserDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.TimeSpentEntity;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Repository.TaskRepository;
import com.example.engineer.Repository.TimeSpentRepository;
import com.example.engineer.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimeSpentService {
    private final TimeSpentRepository timeSpentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TimeSpentEntity get(Long taskId, String email) {
        return timeSpentRepository.find(taskId, email).orElse(null);
    }

    public void create(TimeSpentDTO time) {
        TaskEntity task = taskRepository.findById(time.getTask().getId()).orElseThrow(() -> new NoSuchElementException("Task not found"));

        UserEntity user = userRepository.findByEmail(time.getUser().getEmail()).orElseThrow(() -> new NoSuchElementException("User not found"));

        TimeSpentEntity toSave = new TimeSpentEntity();
        toSave.setTime(time.getTime());
        toSave.setTask(task);
        toSave.setUser(user);

        timeSpentRepository.save(toSave);
    }

    public boolean update(TimeSpentDTO time) {
        TimeSpentEntity toUpdate = get(time.getTask().getId(), time.getUser().getEmail());
        if (toUpdate == null) throw new NoSuchElementException("not found");

        if (time.getTime() != null)
            toUpdate.setTime(time.getTime());

        timeSpentRepository.save(toUpdate);
        return true;
    }

    public TimeSpentByTaskDTO findTimeByTask(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));

        return TimeSpentByTaskDTO.builder()
                .task(TaskMapper.taskDtoWithoutRelations(task))
                .timeSpent(findTimeMapped(task))
                .build();
    }

    public TimeSpentByUserDTO findTimeByUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));

        return TimeSpentByUserDTO.builder()
                .user(UserMapper.userDtoWithoutRelations(user))
                .timeSpent(findTimeMapped(user))
                .build();
    }

    private Map<UserDTO, Double> findTimeMapped(TaskEntity task) {
        Map<UserDTO, Double> time = new HashMap<>();

        Set<TimeSpentEntity> byUsers = timeSpentRepository.findByTask(task.getId());
        if (byUsers.isEmpty()) throw new NoSuchElementException("No time registered for task");

        for (TimeSpentEntity ts : byUsers)
            time.put(UserMapper.userDtoWithoutRelations(ts.getUser()), ts.getTime());

        return time;
    }

    private Map<TaskDTO, Double> findTimeMapped(UserEntity user) {
        Map<TaskDTO, Double> time = new HashMap<>();

        Set<TimeSpentEntity> byTask = timeSpentRepository.findByUser(user.getId());
        if (byTask.isEmpty()) throw new NoSuchElementException("No time registered for user");

        for (TimeSpentEntity ts : byTask)
            time.put(TaskMapper.taskDtoWithoutRelations(ts.getTask()), ts.getTime());

        return time;
    }
}
