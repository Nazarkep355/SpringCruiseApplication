package com.example.SpringCruiseApplication.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {
    @Value("${path.upload}")
    private String path;

    public String saveFile(MultipartFile file) throws IOException {
        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        System.out.println(uploadDir.exists());
        System.out.println(uploadDir.getAbsoluteFile());
        String name = UUID.randomUUID().toString() + "." + (Math.random() * Integer.MAX_VALUE)
                + file.getOriginalFilename();
        File dest = new File(path + "/" + name);
        System.out.println(dest.getAbsoluteFile());
        file.transferTo(dest);
        return name;
    }
}
