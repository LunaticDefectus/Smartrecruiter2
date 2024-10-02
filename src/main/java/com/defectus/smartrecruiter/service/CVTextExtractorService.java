package com.defectus.smartrecruiter.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CVTextExtractorService {

    public String extractTextFromPDF(MultipartFile cvFile) throws IOException {
        try (PDDocument document = PDDocument.load(cvFile.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    public String extractTextFromDOCX(MultipartFile cvFile) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(cvFile.getInputStream())) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        }
    }
}