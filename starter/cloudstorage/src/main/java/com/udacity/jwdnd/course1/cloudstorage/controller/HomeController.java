package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }
    @GetMapping
    public String GetHomePage(Authentication authentication,Model model, @ModelAttribute("newFile") FileForm newFile,
                           @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential){
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNoteListings(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
