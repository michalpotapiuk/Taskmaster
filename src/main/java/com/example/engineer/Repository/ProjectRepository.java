package com.example.engineer.Repository;

import com.example.engineer.Model.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("select p from ProjectEntity p where p.name=:pName")
    ProjectEntity findByName(@Param("pName") String name);

    @Query("select t.project from TaskEntity t where t.id=:taskId")
    ProjectEntity findProjectByTaskId(@Param("taskId") Long taskId);
}
