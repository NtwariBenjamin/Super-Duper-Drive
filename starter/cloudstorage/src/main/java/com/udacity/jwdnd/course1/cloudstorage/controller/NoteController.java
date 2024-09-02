package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    @GetMapping
    public String getHomePage(Authentication authentication,Model model, @ModelAttribute("newFile") FileForm newFile,
                              @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential){
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("notes",this.noteService.getNoteListings(user.getUserId()));
        return "home";
    }

    @PostMapping("add-note")
    public String CreateNote(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        String username=authentication.getName();
        User user=userService.getUser(username);
        String newTitle = newNote.getNoteTitle();
        String noteIdStr = newNote.getNoteId();
        String newDescription = newNote.getNoteDescription();
        if (noteIdStr.isEmpty()) {
            noteService.addNote(newTitle, newDescription, username);
        } else {
            Note existingNote = getNote(Integer.parseInt(noteIdStr));
            noteService.updateNote(existingNote.getNoteId(), newTitle, newDescription);
        }
        model.addAttribute("notes", noteService.getNoteListings(user.getUserId()));
        model.addAttribute("result", "missionAccomplished");

        return "result";
    }

    @GetMapping("/get-note/{noteId}")
    private Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(Authentication authentication,@PathVariable("noteId") Integer noteId,
                             @ModelAttribute("newFile") FileForm fileForm,
                             @ModelAttribute("newCredential") CredentialForm newCredential,
                             @ModelAttribute("newNote")NoteForm newNote, Model model){
        noteService.deleteNote(noteId);
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("notes",this.noteService.getNoteListings(user.getUserId()));
        model.addAttribute("result","missionAccomplished");
        return "result";
    }


}
