package com.example.pdfgenrator.service;


import com.example.pdfgenrator.dto.InvoiceRequest;
import com.example.pdfgenrator.util.FileStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class PdfService {


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ObjectMapper   mapper;

    @Autowired
    private FileStore      store;

    public PdfResult generateOrLoad(@Valid InvoiceRequest req) throws Exception {

        byte[] json = mapper.writeValueAsBytes(req);
        String id = store.idFor(json);

        if (store.exists(id)) {
            return new PdfResult(id, null);
        }

        Context ctx = new Context();
        ctx.setVariables(mapper.convertValue(req, Map.class));
        String html = templateEngine.process("invoice.html", ctx);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(baos);
        builder.run();

        byte[] pdfBytes = baos.toByteArray();

        store.save(id, pdfBytes);
        return new PdfResult(id, pdfBytes);
    }

    public byte[] load(String id) throws IOException { return store.load(id); }

    public record PdfResult(String id, byte[] bytes) {}
}
