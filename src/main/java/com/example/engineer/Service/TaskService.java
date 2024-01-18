package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Task.TaskDTODashboard;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Repository.TaskRepository;
import com.example.engineer.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDTO create(TaskDTO task) {
        TaskEntity mappedTask = TaskMapper.mapToEntity(task);
        TaskEntity savedTask = taskRepository.save(mappedTask);
        return TaskMapper.mapToDto(savedTask);
    }

    public Optional<TaskDTO> findForGetEndpointById(Long id) {
        return taskRepository
                .findById(id)
                .map(TaskMapper::mapToDtoForGetTaskMethod);
    }


    public Set<TaskDTODashboard> findUserTasks(UserDTO userDTO) {
        Set<TaskDTODashboard> tasksDTOs = new HashSet<>();
        Set<TaskEntity> tasks = taskRepository.findTasksByUserId(userDTO.getId());

        for (TaskEntity task : tasks) {
            tasksDTOs.add(TaskMapper.mapToDtoForDashboard(task));
        }
        return tasksDTOs;
    }

    public Optional<TaskDTO> findById(Long id) {
        return taskRepository
                .findById(id)
                .map(TaskMapper::mapToDto);
    }

    public void setAsSubtask(TaskDTO taskDTO, TaskDTO parentDTO) {
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);
        TaskEntity parent = TaskMapper.mapToEntity(parentDTO);
        task.setParentTask(parent);
        taskRepository.save(task);
    }

    public void addTaskToUser(TaskDTO taskDTO, UserDTO userDTO) {
        UserEntity user = UserMapper.mapToEntity(userDTO);
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);
        user.getTasks().add(task);
        userRepository.save(user);
    }

    public Set<TaskDTO> findSubtasks(TaskDTO taskDTO) {
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);
        return TaskMapper.subtaskList(task);
    }

    // TODO: 12/11/2023 Moze do wyjebania 
    public Set<TaskDTO> findTasksByProject(ProjectDTO project) {
        Set<TaskDTO> tasksDTOs = new HashSet<>();
        Set<TaskEntity> tasks = taskRepository.getTaskByProject(project.getId());

        if (!tasks.isEmpty())
            for (TaskEntity task : tasks)
                tasksDTOs.add(TaskMapper.taskDtoWithoutRelations(task));

        return tasksDTOs;
    }

    @Transactional
    public void delete(TaskDTO taskDTO) {
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);

        // Usuwanie zadania z kolekcji użytkowników
        Set<UserEntity> users = task.getUsers();
        if (users != null) {
            for (UserEntity user : users) {
                user.getTasks().remove(task);
                userRepository.save(user);
            }
        }

        // Usuwanie zadania z kolekcji zadań użytkowników
        userRepository.findAll().forEach(user -> user.getTasks().removeIf(t -> t.getId().equals(task.getId())));

        // Usuwanie zadania z projektu
        task.setProject(null);

        // Usuwanie referencji do zadania z rodzica i od rodzica
        if (task.getParentTask() != null) {
            task.getParentTask().getSubtasks().remove(task);
            taskRepository.save(task.getParentTask());
            task.setParentTask(null);
        }

        // Usuwanie referencji do zadań podrzędnych i od zadań podrzędnych
        Set<TaskEntity> subs = task.getSubtasks();
        if (!subs.isEmpty()) {
            for (TaskEntity sub : subs) {
                sub.setParentTask(null);
                taskRepository.save(sub);
            }
        }

        // Ustawienie pustej kolekcji użytkowników dla zadania
        task.setUsers(new HashSet<>());

        // Usuwanie zadania
        taskRepository.save(task);
        taskRepository.delete(task);
    }


    public void update(Long id, TaskDTO task) {
        task.setId(id);
        TaskEntity updateTask = TaskMapper.mapToEntity(task);
        taskRepository.save(updateTask);
    }
}
