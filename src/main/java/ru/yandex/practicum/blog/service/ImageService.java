package ru.yandex.practicum.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    private final String serverPath;
    private final String clientPath;

    public ImageService(@Value("${image.folder.server.path}") String serverPath,
                            @Value("${image.folder.client.path}") String clientPath) {
        this.serverPath = serverPath;
        this.clientPath = clientPath;
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
            return Optional.of(clientPath + fileName);
        } catch (IOException e) {
            log.error("Ошибка сохранения файла", e);
            return Optional.empty();
        }
    }
}
