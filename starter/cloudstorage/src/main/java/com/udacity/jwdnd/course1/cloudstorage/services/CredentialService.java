package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
    }

    public Credential[] getCredentialListings(Integer userId) {
        return credentialMapper.getCredentialListing(userId);
    }

    public void addCredential(String newUrl, String username, String credentialUsername, String encodedKey, String encryptedPassword) {
       Integer userId=userMapper.getUser(username).getUserId();
       Credential credential=new Credential(0,newUrl,credentialUsername,encodedKey,encryptedPassword,userId);
       credentialMapper.addCredentials(credential);

    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void updateCredential(Integer credentialId, String username, String newUrl, String encodedKey, String encryptedPassword) {
        credentialMapper.updateCredentials(credentialId,newUrl,username,encodedKey,encryptedPassword);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredentials(credentialId);
    }
}
