package com.example.pdfgenrator.controller;



import com.example.pdfgenrator.dto.InvoiceRequest;
import com.example.pdfgenrator.model.InvoiceData;
import com.example.pdfgenrator.service.PdfService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService service;


    @PostMapping
    public ResponseEntity<byte[]> create(@Valid @RequestBody InvoiceData req) throws Exception {

        var result = service.generateOrLoad(req);


        byte[] pdf = (result.bytes() == null) ? service.load(result.id()) : result.bytes();

        return ResponseEntity.ok()
                .header("X-Pdf-Id", result.id())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {
        byte[] pdf = service.load(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+id+".pdf\"")
                .body(pdf);
    }

    @PostMapping("/test")
    public String echo(@RequestBody InvoiceRequest req) throws Exception {
        System.out.println("Invoice Number: " + req.getInvoiceNumber());
        System.out.println("Customer Name: " + req.getCustomerName());
        System.out.println("Items: " + req.getItems());
        System.out.println("Total: " + req.getTotal());
        return "OK";
    }
}
