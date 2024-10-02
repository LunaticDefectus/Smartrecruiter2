package com.defectus.smartrecruiter.web;

import com.defectus.smartrecruiter.dao.entities.Application;
import com.defectus.smartrecruiter.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadCv(@PathVariable Integer id) {
        Application application = applicationService.findApplicationById(id)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        byte[] cvFile = application.getCvText();
        String fileType = application.getFileType();
        String originalFilename = application.getCvFilename();

        // Log the file details
        System.out.println("File Size: " + cvFile.length);
        System.out.println("Original Filename: " + originalFilename);

        if (!"application/pdf".equals(fileType)) {
            throw new IllegalStateException("The file type is not a PDF");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cvFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + originalFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }




}
