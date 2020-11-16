package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class StorageService {

    public void store(MultipartFile multipartFile, File file) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(multipartFile.getInputStream());
             BufferedOutputStream out = new BufferedOutputStream(openOutputStream(file))
        ) {
            copy(in, out);
        }
    }

    public static void copy(BufferedInputStream in, BufferedOutputStream out) throws IOException {
        byte[] buff = new byte[32 * 1024];
        int len = 0;
        while ((len = in.read(buff)) > 0) {
            out.write(buff, 0, len);
        }
    }

    private static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
            return new FileOutputStream(file);
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = Paths.get(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }
}
