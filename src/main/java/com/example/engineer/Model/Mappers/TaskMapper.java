package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Issue.IssueDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.Task.TaskDTODashboard;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.IssueEntity;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.NoteEntity;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.TimeSpentEntity;
import com.example.engineer.Model.User.UserEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskMapper {
    public static TaskDTO mapToDto(TaskEntity task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setStartDate(task.getStartDate());
        taskDTO.setEndDate(task.getEndDate());
        taskDTO.setDescription(task.getDescription());

        if (task.getUsers() != null) {
            Set<UserDTO> userDTOs = new HashSet<>();
            for (UserEntity user : task.getUsers()) {
                userDTOs.add(UserMapper.userDtoWithoutRelations(user));
            }
            taskDTO.setPersons(userDTOs);
        }

        if (task.getProject() != null) {
            taskDTO.setProject(ProjectMapper.projectDtoWithoutRelations(task.getProject()));
        }

        if (task.getTimeSpents() != null) {
            Set<TimeSpentDTO> timeSpentDTOs = task.getTimeSpents().stream()
                    .map(TimeSpentMapper::map)
                    .collect(Collectors.toSet());
            taskDTO.setTimeSpent(timeSpentDTOs);
        }

        Set<IssueDTO> issueDTOs = new HashSet<>();
        if (task.getIssues() != null) {
            for (IssueEntity issue : task.getIssues()) {
                issueDTOs.add(IssueMapper.mapToDto(issue));
            }
        }
        taskDTO.setIssues(issueDTOs);

        if (task.getReport() != null) {
            taskDTO.setReport(ReportMapper.mapToDto(task.getReport()));
        }

        Set<NoteDTO> noteDTOs = new HashSet<>();
        if (task.getNotes() != null) {
            for (NoteEntity note : task.getNotes()) {
                noteDTOs.add(NoteMapper.mapToDto(note));
            }
        }
        taskDTO.setNotes(noteDTOs);

        if (task.getParentTask() != null) {
            if (!task.getParentTask().getId().equals(task.getId())) {
                taskDTO.setParentTask(taskDtoWithoutRelations(task.getParentTask()));
            }
        }

        Set<TaskDTO> subtaskDTOs = new HashSet<>();
        if (task.getSubtasks() != null) {
            for (TaskEntity subtask : task.getSubtasks()) {
                if (!subtask.getId().equals(task.getId())) {
                    subtaskDTOs.add(taskDtoWithoutRelations(subtask));
                }
            }
        }
        taskDTO.setSubtasks(subtaskDTOs);

        return taskDTO;
    }

    public static TaskEntity mapToEntity(TaskDTO taskDTO) {
        TaskEntity task = new TaskEntity();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setDescription(taskDTO.getDescription());

        if (taskDTO.getPersons() != null) {
            Set<UserEntity> users = new HashSet<>();
            for (UserDTO userDTO : taskDTO.getPersons()) {
                if (userDTO != null) {
                    users.add(UserMapper.mapToEntity(userDTO));
                }
            }
            task.setUsers(users);
        }

        if (taskDTO.getProject() != null) {
            task.setProject(ProjectMapper.mapToEntity(taskDTO.getProject()));
        }

        if (taskDTO.getTimeSpent() != null) {
            Set<TimeSpentEntity> timeSpents = taskDTO.getTimeSpent().stream()
                    .map(TimeSpentMapper::map)
                    .collect(Collectors.toSet());
            task.setTimeSpents(timeSpents);
        }

        if (taskDTO.getIssues() != null) {
            Set<IssueEntity> issues = new HashSet<>();
            for (IssueDTO issueDTO : taskDTO.getIssues()) {
                issues.add(IssueMapper.mapToEntity(issueDTO));
            }
            task.setIssues(issues);
        }

        if (taskDTO.getReport() != null) {
            task.setReport(ReportMapper.mapToEntity(taskDTO.getReport()));
        }

        if (taskDTO.getNotes() != null) {
            Set<NoteEntity> notes = new HashSet<>();
            for (NoteDTO noteDTO : taskDTO.getNotes()) {
                notes.add(NoteMapper.mapToEntity(noteDTO));
            }
            task.setNotes(notes);
        }

        if (taskDTO.getParentTask() != null) {
            task.setParentTask(mapToEntity(taskDTO.getParentTask()));
        }

        if (taskDTO.getSubtasks() != null) {
            Set<TaskEntity> subtasks = new HashSet<>();
            for (TaskDTO subtaskDTO : taskDTO.getSubtasks()) {
                subtasks.add(mapToEntity(subtaskDTO));
            }
            task.setSubtasks(subtasks);
        }
        return task;
    }

    public static TaskDTO taskDtoWithoutRelations(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return TaskDTO.builder().build();
        }
        return TaskDTO.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .status(taskEntity.getStatus())
                .priority(taskEntity.getPriority())
                .startDate(taskEntity.getStartDate())
                .endDate(taskEntity.getEndDate())
                .build();
    }

    public static Set<TaskDTO> subtaskList(TaskEntity task) {
        Set<TaskDTO> subtasks = new HashSet<>();
        Set<TaskEntity> tasks = task.getSubtasks();
        if (!tasks.isEmpty())
            for (TaskEntity taskEntity : tasks)
                subtasks.add(TaskMapper.taskDtoWithoutRelations(taskEntity));

        return subtasks;
    }


    public static TaskDTO mapToDtoForGetTaskMethod(TaskEntity task) {
        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .priority(task.getPriority())
                .status(task.getStatus())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .description(task.getDescription())
                .project(ProjectMapper.projectDtoWithoutRelations(task.getProject()))
                .issues(IssueMapper.issueDtoList(task))
                .notes(NoteMapper.noteDtoList(task))
                .parentTask(TaskMapper.taskDtoWithoutRelations(task.getParentTask()))
                .subtasks(subtaskList(task))
                .build();
    }


    public static TaskDTODashboard mapToDtoForDashboard(TaskEntity task) {
        return TaskDTODashboard.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .projectName(ProjectMapper.mapToDto(task.getProject()).getName())
                .build();
    }
}
