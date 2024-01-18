package com.example.engineer.Model;


import com.example.engineer.Model.Enums.Priority;
import com.example.engineer.Model.Enums.Status;
import com.example.engineer.Model.User.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Status status;
    private Priority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<UserEntity> users;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<TimeSpentEntity> timeSpents;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<IssueEntity> issues;


    @OneToOne(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private ReportEntity report;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<NoteEntity> notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_task_id")
    private TaskEntity parentTask;

    @OneToMany(mappedBy = "parentTask", fetch = FetchType.EAGER)
    private Set<TaskEntity> subtasks;
}
