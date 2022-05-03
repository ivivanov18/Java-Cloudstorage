package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;

    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping
    public String addCredential(@ModelAttribute Credential credential, Model model) {
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
