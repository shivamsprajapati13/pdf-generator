package com.example.pdfgenrator.controller;

import com.example.pdfgenrator.model.InvoiceData;
import com.example.pdfgenrator.service.PdfService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService service;


    @PostMapping("/download")
    public ResponseEntity<?> downloadPdf(@Valid @RequestBody InvoiceData req) throws Exception {

        var result = service.generateOrLoad(req);

        if (result.bytes() == null) {
            String downloadUrl = "/pdf/" + result.id();
            return ResponseEntity.ok(Map.of(
                    "message", "PDF already exists",
                    "downloadUrl", downloadUrl,
                    "filename", result.id() + ".pdf"
            ));
        }

        String fileName = result.id() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .header("X-Filename", fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(result.bytes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {
        byte[] pdf = service.load(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".pdf\"")
                .body(pdf);
    }
}
