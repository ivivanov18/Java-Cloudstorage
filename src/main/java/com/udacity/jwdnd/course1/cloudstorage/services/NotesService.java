package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    private final NoteMapper noteMapper;

    public NotesService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes(int userId) {
        return noteMapper.getNotes(userId);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public int addNote(Note note) {
        if (note.getNoteId() == null) {
            return noteMapper.insert(note);
        } else {
            return noteMapper.update(note);
        }
    }

    public int deleteNote(Integer noteId) {
        return noteMapper.delete(noteId);
    }
}
