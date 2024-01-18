package com.example.engineer.Repository;

import com.example.engineer.Model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("select t from TaskEntity t where t.parentTask.id=:taskId")
    Set<TaskEntity> getSubtasks(@Param("taskId") Long taskId);

    @Query("select t from TaskEntity t where t.project.id=:projectId")
    Set<TaskEntity> getTaskByProject(@Param("projectId") Long projectId);

    @Query("select t from TaskEntity t INNER join t.users u where u.id = :userId")
    Set<TaskEntity> findTasksByUserId(@Param("userId") Long userId);
}
