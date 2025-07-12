
package com.example.pdfgenrator.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class FileStore {

    private final Path root = Paths.get("src/main/resources/pdf-store");


    public String idFor(byte[] jsonBytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(jsonBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public Path pathFor(String id) { return root.resolve(id + ".pdf"); }

    public boolean exists(String id) { return Files.exists(pathFor(id)); }

    public void save(String id, byte[] pdf) throws IOException {
        Files.write(pathFor(id), pdf, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public byte[] load(String id) throws IOException {
        return Files.readAllBytes(pathFor(id));
    }
}
