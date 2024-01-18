package com.example.engineer.Repository;

import com.example.engineer.Model.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    @Query("select i from IssueEntity i where i.task.id=:taskId")
    Set<IssueEntity> getIssueByTask(@Param("taskId") Long taskId);

    Set<IssueEntity> findAllByTaskIsNull();
}
