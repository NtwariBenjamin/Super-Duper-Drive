package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }
    @GetMapping
    public String GetHomePage(Authentication authentication,Model model, @ModelAttribute("newFile") FileForm newFile,
                              @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential){
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        return "home";
    }

    @PostMapping("add-file")
    public String newFile(Authentication authentication, @ModelAttribute("newFile")FileForm newFile,
                          @ModelAttribute("newNote")NoteForm newNote, @ModelAttribute("newCredential")CredentialForm newCredential,
                          Model model) throws IOException {
        String username=authentication.getName();
        User user=userService.getUser(username);
        String[] allFiles=fileService.getAllFiles(user.getUserId());
        MultipartFile multipartFile=newFile.getFile();
        String fileName=multipartFile.getOriginalFilename();
        boolean ExistingFile=false;
        for (String file: allFiles){
            if (file.equals(fileName)){
                ExistingFile=true;
                break;
            }
        }
        if (!ExistingFile){
            fileService.addFile(multipartFile,username);
            model.addAttribute("result","missionAccomplished");
        }else {
            model.addAttribute("result","error");
            model.addAttribute("message","You have tried to add a duplicate file.");
        }
        model.addAttribute("files",fileService.getAllFiles(user.getUserId()));
        return "result";
    }
    @GetMapping(value = "/get-file/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile( Authentication authentication, @PathVariable String fileName, @ModelAttribute("newFile") FileForm newFile,
                              @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
                              Model model){
        String username=authentication.getName();
        User user=userService.getUser(username);
        fileService.deleteFile(fileName);
        model.addAttribute("files",fileService.getAllFiles(user.getUserId()));
        model.addAttribute("result","missionAccomplished");
        return "result";
    }
}
