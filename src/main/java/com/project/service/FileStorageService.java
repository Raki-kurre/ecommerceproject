package com.project.service;

import java.io.File;
import java.io.IOException;



import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/profile-images/";

    public String saveProfileImage(MultipartFile file) throws IOException {

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // ✅ create folders
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(dir, fileName);

        file.transferTo(dest);
        return fileName;
    }
}