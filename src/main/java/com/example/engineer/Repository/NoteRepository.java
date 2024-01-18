package com.example.engineer.Repository;

import com.example.engineer.Model.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    @Query("select n from NoteEntity n where n.diagram.id=:diagramId")
    List<NoteEntity> getAllNotes(@Param("diagramId") Long diagramId);

    @Query("select n from NoteEntity n where n.task.id=:taskId")
    Set<NoteEntity> getNoteByTask(@Param("taskId") Long taskId);

    @Query("select n from NoteEntity n join fetch n.user u where u.id = :userId")
    Set<NoteEntity> findNoteEntitiesByUser(@Param("userId") Long userId);

}
