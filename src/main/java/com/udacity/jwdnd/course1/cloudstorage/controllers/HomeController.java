package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RequestMapping("/home")
@Controller
public class HomeController {
    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private final FilesService filesService;
    private final UserService userService;

    public HomeController(NotesService notesService, CredentialsService credentialsService, FilesService filesService, UserService userService) {
        this.notesService = notesService;
        this.credentialsService = credentialsService;
        this.filesService = filesService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Principal principal, @ModelAttribute Note note, @ModelAttribute Credential credential, @ModelAttribute("newFile") FileView file, Model model) {
        String userName = principal.getName();
        User loggedUser = this.userService.getUser(userName);

        if (loggedUser == null) {
            model.addAttribute("error", "Forbidden.");
            return "result";
        }
        model.addAttribute("allNotes", this.notesService.getAllNotes(loggedUser.getUserId()));
        model.addAttribute("allCredentials", this.credentialsService.getAllCredentials(loggedUser.getUserId()));
        model.addAttribute("allFiles", this.filesService.getAllFiles(loggedUser.getUserId()));
        return "home";
    }
}
