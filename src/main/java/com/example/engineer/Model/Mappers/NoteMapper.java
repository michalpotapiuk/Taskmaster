package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.NoteEntity;
import com.example.engineer.Model.TaskEntity;

import java.util.HashSet;
import java.util.Set;


public class NoteMapper {

    public static NoteEntity mapToEntity(NoteDTO noteDTO) {
        NoteEntity note = new NoteEntity();
        note.setId(noteDTO.getId());
        note.setNote(noteDTO.getNote());
        if (noteDTO.getProject() != null) {
            note.setProject(ProjectMapper.mapToEntity(noteDTO.getProject()));
        }
        if (noteDTO.getTask() != null) {
            note.setTask(TaskMapper.mapToEntity(noteDTO.getTask()));
        }
        if (noteDTO.getUser() != null) {
            note.setUser(UserMapper.mapToEntity(noteDTO.getUser()));
        }
        if (noteDTO.getDiagram() != null) {
            note.setDiagram(DiagramMapper.mapToEntity(noteDTO.getDiagram()));
        }
        return note;
    }

    public static NoteDTO mapToDto(NoteEntity note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setNote(note.getNote());

        if (note.getProject() != null) {
            noteDTO.setProject(ProjectMapper.projectDtoWithoutRelations(note.getProject()));
        }

        if (note.getTask() != null) {
            noteDTO.setTask(TaskMapper.mapToDto(note.getTask()));
        }

        if (note.getUser() != null) {
            noteDTO.setUser(UserMapper.userDtoWithoutRelations(note.getUser()));
        }

        if (note.getDiagram() != null) {
            noteDTO.setDiagram(DiagramMapper.mapToDto(note.getDiagram()));
        }

        return noteDTO;
    }


    public static NoteDTO noteDtoWithoutRelations(NoteEntity noteEntity) {
        return noteEntity == null ? null : NoteDTO.builder()
                .id(noteEntity.getId())
                .note(noteEntity.getNote())
                .build();
    }

    public static Set<NoteDTO> noteDtoList(TaskEntity task) {
        Set<NoteEntity> notes = task.getNotes();
        Set<NoteDTO> noteSet = new HashSet<>();

        if (notes.isEmpty()) {
            return null;
        }

        for (NoteEntity note : notes)
            noteSet.add(noteDtoWithoutRelations(note));

        return noteSet;
    }
}
