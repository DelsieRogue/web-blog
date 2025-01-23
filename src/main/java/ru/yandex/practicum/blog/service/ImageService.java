package ru.yandex.practicum.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    private final String serverPath;

    public ImageService(@Value("${image.folder.server.path}") String serverPath) {
        this.serverPath = serverPath;
    }

    public Optional<String> save(MultipartFile image) {
        if (image.isEmpty()) {
            log.warn("Файл пустой, загрузка не выполнена.");
            return Optional.empty();
        }
        try {
            String extension = FilenameUtils.getExtension(image.getOriginalFilename());
            String fileName = "file_" + UUID.randomUUID() + "." + extension;
            File destinationFile = new File(serverPath, fileName);
            image.transferTo(destinationFile);
            return Optional.of(fileName);
        } catch (IOException e) {
            log.error("Ошибка сохранения файла", e);
            return Optional.empty();
        }
    }

    public void deleteImage(String imageName) {
        try {
            Path path = Paths.get(serverPath + imageName);
            Files.delete(path);
            log.info("Файл {} удалён" + imageName);
        } catch (IOException e) {
            log.error("Ошибка при удалении файла {}", imageName, e);
        }
    }
}
