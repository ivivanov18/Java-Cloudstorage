package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/notes")
public class NotesController {
    private final NotesService notesService;
    private final UserService userService;

    public NotesController(NotesService notesService, UserService userService) {
        this.notesService = notesService;
        this.userService = userService;
    }

    @PostMapping
    public String createNote(@ModelAttribute Note note, Model model, Principal principal) {
        String userName = principal.getName();
        User loggedUser = this.userService.getUser(userName);
        note.setUserId(loggedUser.getUserId());
        int rowsAdd = notesService.addNote(note);
        if(rowsAdd < 0) {
            model.addAttribute("error", "Error while creating note");
        } else {
            model.addAttribute("success", true);
        }
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
