package com.example.engineer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "report")
public class ReportEntity {

    @Id
    private Long id;
    private String content;

    @OneToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

}
