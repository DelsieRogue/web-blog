package ru.yandex.practicum.blog.service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = ImageService.class)
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Test
    void save() throws IOException {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("names.png");
        Optional<String> save = imageService.save(multipartFile);
        verify(multipartFile, times(1)).transferTo(any(File.class));
        assertTrue(save.isPresent() && save.get().startsWith("file") && save.get().endsWith("png"));

    }

    @Test
    void save_empty() throws IOException {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(Boolean.TRUE);
        imageService.save(multipartFile);
        verify(multipartFile, times(0)).transferTo(any(File.class));
    }

    @Test
    void deleteImage() {
        Path path = mock(Path.class);
        try (MockedStatic<Paths> mockedStaticPaths = mockStatic(Paths.class);
             MockedStatic<Files> mockedStaticFiles = mockStatic(Files.class)) {
            mockedStaticPaths.when(() -> Paths.get(anyString())).thenReturn(path);
            imageService.deleteImage("img.png");
            mockedStaticPaths.verify(() -> Paths.get(matches("img.png")), times(1));
            mockedStaticFiles.verify(() -> Files.delete(any(Path.class)), times(1));
        }
    }
}