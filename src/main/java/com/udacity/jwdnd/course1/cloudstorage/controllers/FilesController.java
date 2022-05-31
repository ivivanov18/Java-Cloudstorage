package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/files")
public class FilesController {
    private final static int MAX_ALLOWED_UPLOAD_SIZE_IN_BYTES = 10000000;
    private final FilesService filesService;
    private final UserService userService;

    public FilesController(FilesService filesService, UserService userService) {
        this.filesService = filesService;
        this.userService = userService;
    }

    @PostMapping
    public String uploadFile(@ModelAttribute("newFile") MultipartFile file, Principal principal, Model model) {
        String userName = principal.getName();
        User loggedUser = this.userService.getUser(userName);

        if (file.isEmpty()) {
            model.addAttribute("error", "File is empty and cannot be uploaded");
            return "result";
        }

        if (file.getSize() > MAX_ALLOWED_UPLOAD_SIZE_IN_BYTES) {
            model.addAttribute("error", "File size is larger than allowed");
            return "result";
        }

        if (!filesService.isFileNameAvailable(file.getOriginalFilename(), loggedUser.getUserId())) {
            model.addAttribute("error", "Please retry. The file name is not available");
            return "result";
        }

        int rowAdded = filesService.saveFile(file, loggedUser.getUserId());

        if (rowAdded < 0) {
            model.addAttribute("error", "Could not upload file");
            return "result";
        }
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
        int rowDeleted = this.filesService.deleteFile(fileId);

        if (rowDeleted < 1) {
            model.addAttribute("error", "Error while deleting the file.");
        } else {
            model.addAttribute("success", true);
        }
        return "result";
    }

    @GetMapping("/view/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@PathVariable("fileId") Integer fileId, Model model) {
        File file = this.filesService.loadFileById(fileId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"").body(file.getFileData());
    }
}
