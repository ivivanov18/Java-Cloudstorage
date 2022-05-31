package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialsService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getAllCredentials(int userId) {
        List<Credential> credentials = credentialMapper.getAllCredentials(userId);

        for (Credential credential: credentials) {
            credential.setDecryptedPassword(this.encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        }
        return credentials;
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public int addCredential(Credential credential) {
        String key = this.encryptionService.generateEncodedKey();
        String encryptedPassword = this.encryptionService.encryptValue(credential.getPassword(), key);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        if (credential.getCredentialId() == null) {
            return credentialMapper.insert(credential);
        } else {
            return credentialMapper.updateCredential(credential);
        }
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }
}
