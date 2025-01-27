package ru.yandex.practicum.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;
import ru.yandex.practicum.blog.service.CommentService;
import ru.yandex.practicum.blog.service.ImageService;
import ru.yandex.practicum.blog.service.PostService;

@Configuration
@Profile("unit-test")
public class ServiceBeanConfig {

    @Bean
    public ImageService imageService() {
        return new ImageService("/test");
    }

    @Bean
    public PostService postService(PostDao postDao, CommentDao commentDao, ImageService imageService) {
        return new PostService(postDao, commentDao, imageService);
    }

    @Bean
    public CommentService commentService(CommentDao commentDao) {
        return new CommentService(commentDao);
    }
}
