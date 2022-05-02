package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
@Controller
public class HomeController {
    private final NotesService notesService;
    private final CredentialsService credentialsService;

    public HomeController(NotesService notesService, CredentialsService credentialsService) {
        this.notesService = notesService;
        this.credentialsService = credentialsService;
    }

    @GetMapping
    public String getHomePage(@ModelAttribute Note note, @ModelAttribute Credential credential, Model model) {
        model.addAttribute("allNotes", this.notesService.getAllNotes());
        model.addAttribute("allCredentials", this.credentialsService.getAllCredentials());
        return "home";
    }
}
