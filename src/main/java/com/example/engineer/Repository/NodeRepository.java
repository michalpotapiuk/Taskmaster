package com.example.engineer.Repository;

import com.example.engineer.Model.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    @Query("select n from NodeEntity n where n.diagram.id=:diagramId")
    List<NodeEntity> getAllNodes(@Param("diagramId") Long diagramId);
}
