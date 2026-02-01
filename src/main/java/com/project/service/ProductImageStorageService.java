package com.project.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductImageStorageService {

    private static final String UPLOAD_DIR = "/tmp/product-images";

    public String save(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName =
                System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dest = new File(dir, fileName);
        file.transferTo(dest);

        return fileName;
    }
}
