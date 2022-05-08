package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @PostMapping
    public String addCredential(@ModelAttribute Credential credential, Principal principal, Model model) {
        String userName = principal.getName();
        User loggedUser = this.userService.getUser(userName);

        credential.setUserId(loggedUser.getUserId());

        int rowAdded = credentialsService.addCredential(credential);
        if (rowAdded < 0) {
            model.addAttribute("error", "Error while adding credential");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Model model) {
        int deletedRow = credentialsService.deleteCredential(credentialId);
        if (deletedRow < 1) {
            model.addAttribute("error", "Credential could not be deleted");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }
}
