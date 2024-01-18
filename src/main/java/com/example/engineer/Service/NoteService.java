package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Diagram.DiagramDTO;
import com.example.engineer.Model.DTO.Note.NoteDTO;
import com.example.engineer.Model.DTO.Project.ProjectDTO;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.DTO.User.UserDTO;
import com.example.engineer.Model.DiagramEntity;
import com.example.engineer.Model.Mappers.DiagramMapper;
import com.example.engineer.Model.Mappers.NoteMapper;
import com.example.engineer.Model.Mappers.ProjectMapper;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.NoteEntity;
import com.example.engineer.Model.ProjectEntity;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteDTO create(NoteDTO noteDTO) {
        NoteEntity mappedNote = NoteMapper.mapToEntity(noteDTO);
        NoteEntity savedNote = noteRepository.save(mappedNote);
        return NoteMapper.mapToDto(savedNote);
    }

    public Optional<NoteDTO> findById(Long id) {
        return noteRepository
                .findById(id)
                .map(NoteMapper::mapToDto);
    }

    public Set<NoteDTO> findUserNotes(UserDTO userDTO) {
        Set<NoteDTO> noteDTOS = new HashSet<>();
        Set<NoteEntity> notes = noteRepository.findNoteEntitiesByUser(userDTO.getId());

        for (NoteEntity note : notes) {
            noteDTOS.add(NoteMapper.mapToDto(note));
        }
        return noteDTOS;
    }

    public void addNoteToProject(NoteDTO noteDTO, ProjectDTO projectDTO) {
        ProjectEntity project = ProjectMapper.mapToEntity(projectDTO);
        NoteEntity note = NoteMapper.mapToEntity(noteDTO);
        note.setProject(project);
        noteRepository.save(note);
    }

    public void addNoteToTask(NoteDTO noteId, TaskDTO taskId) {
        NoteEntity note = NoteMapper.mapToEntity(noteId);
        TaskEntity task = TaskMapper.mapToEntity(taskId);
        note.setTask(task);
        noteRepository.save(note);
    }

    public void addNoteToUser(NoteDTO noteDTO, UserDTO userDTO) {
        NoteEntity note = NoteMapper.mapToEntity(noteDTO);
        UserEntity user = UserMapper.mapToEntity(userDTO);
        note.setUser(user);
        noteRepository.save(note);
    }

    public void addNoteToDiagram(NoteDTO noteDTO, DiagramDTO diagramDTO) {
        NoteEntity note = NoteMapper.mapToEntity(noteDTO);
        DiagramEntity diagram = DiagramMapper.mapToEntity(diagramDTO);
        note.setDiagram(diagram);
        noteRepository.save(note);
    }

    @Transactional
    public void delete(NoteDTO noteDTO) {
        NoteEntity note = NoteMapper.mapToEntity(noteDTO);

        //remove project
        note.setProject(null);

        //remove task
        note.setTask(null);

        //remove user
        note.setUser(null);

        //remove diagram
        note.setDiagram(null);

        noteRepository.save(note);
        noteRepository.delete(note);
    }

    public void update(Long id, NoteDTO noteDTO) {
        noteDTO.setId(id);
        NoteEntity note = NoteMapper.mapToEntity(noteDTO);
        noteRepository.save(note);
    }
}
