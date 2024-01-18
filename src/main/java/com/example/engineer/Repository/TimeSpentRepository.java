package com.example.engineer.Repository;

import com.example.engineer.Model.TimeSpentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TimeSpentRepository extends JpaRepository<TimeSpentEntity, Long> {
    @Query("select ts from TimeSpentEntity ts where ts.task.id=:taskId and ts.user.email=:email")
    Optional<TimeSpentEntity> find(@Param("taskId") Long taskId, @Param("email") String email);

    @Query("select ts from TimeSpentEntity ts where ts.user.id=:userId")
    Set<TimeSpentEntity> findByUser(@Param("userId") Long userId);

    @Query("select ts from TimeSpentEntity ts where ts.task.id=:taskId")
    Set<TimeSpentEntity> findByTask(@Param("taskId") Long taskId);
}
