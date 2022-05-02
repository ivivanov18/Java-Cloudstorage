package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public String createNote(@ModelAttribute Note note, Model model) {
        int rowsAdd = notesService.addNote(note);
        if(rowsAdd < 0) {
            model.addAttribute("error", "Error while creating note");
        } else {
            model.addAttribute("success", true);
        }
        model.addAttribute("allNotes", this.notesService.getAllNotes());
        return "result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId, Model model) {
        int rowDeleted = notesService.deleteNote(noteId);

        if (rowDeleted < 1) {
            model.addAttribute("error", "Error while deleting the note");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }
}
