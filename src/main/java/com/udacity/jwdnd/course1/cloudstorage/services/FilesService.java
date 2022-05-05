package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FilesService {

    private final FileMapper fileMapper;

    public FilesService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int saveFile(MultipartFile multipartFile) {
        try {
            File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), null, multipartFile.getBytes());
            return fileMapper.insert(file);
        } catch (IOException io) {
            return -1;
        }
    }

    public boolean isFileNameAvailable(String fileName) {
        return fileMapper.getFileByFileName(fileName) == null;
    }

    public List<File> getAllFiles() {
        return fileMapper.getFiles();
    }

    public int deleteFile(int fileId) {
        return fileMapper.delete(fileId);
    }

    public File loadFileById(int fileId) {
        return fileMapper.getFileByFileId(fileId);
    }
}
