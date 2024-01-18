package com.example.engineer.Repository;

import com.example.engineer.Model.DiagramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface DiagramRepository extends JpaRepository<DiagramEntity, Long> {

    @Query("select d from DiagramEntity d where d.title=:title")
    Optional<DiagramEntity> findDiagramByTitle(@Param("title") String title);

    @Query("select d from DiagramEntity d where d.description=:description")
    Optional<DiagramEntity> findDiagramByDescription(@Param("description") String description);

    @Query("select distinct d from DiagramEntity d join fetch d.nodes where d.id in (select d2.id from DiagramEntity d2 join d2.nodes n where n.id = :diagramId)")
    Set<DiagramEntity> findDiagramEntitiesWithNodesById(@Param("diagramId") Long diagramId);

}
