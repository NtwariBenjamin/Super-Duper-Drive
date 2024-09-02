package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }
    public String getHomePage(Authentication authentication,Model model, @ModelAttribute("newFile") FileForm newFile,
                              @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential){
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("credentials",this.credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }
    @PostMapping("add-credential")
    public String newCredential(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                                @ModelAttribute("newCredential") CredentialForm newCredential,
                                @ModelAttribute("newNote") NoteForm newNote, Model model){

        String username=authentication.getName();
        User user=userService.getUser(username);
        String newUrl=newCredential.getUrl();
        String credentialId=newCredential.getCredentialId();
        String password=newCredential.getPassword();
        SecureRandom random=new SecureRandom();
        byte[] key=new byte[16];
        random.nextBytes(key);
        String encodedKey= Base64.getEncoder().encodeToString(key);
        String encryptedPassword=encryptionService.encryptValue(password,encodedKey);

        if (credentialId.isEmpty()){
            credentialService.addCredential(newUrl, username, newCredential.getUsername(), encodedKey, encryptedPassword);
        }else {
            credentialService.updateCredential(getCredential(Integer.parseInt(credentialId)).getCredentialId(),newCredential.getUsername(),newUrl,encodedKey,encryptedPassword);
        }
        model.addAttribute("credentials",credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService",encryptionService);
        model.addAttribute("result","missionAccomplished");
        return "result";
    }
    @GetMapping("get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }
    @GetMapping(value = "delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId,Authentication authentication,
                                   @ModelAttribute("newNote")NoteForm newNote,@ModelAttribute("newFile")
                                   FileForm newFile,@ModelAttribute("newCredential")CredentialForm newCredential,
                                   Model model){
        credentialService.deleteCredential(credentialId);
        String username=authentication.getName();
        User user=userService.getUser(username);
        model.addAttribute("credentials",credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService",encryptionService);
        model.addAttribute("result","missionAccomplished");
        return "result";
    }

}
