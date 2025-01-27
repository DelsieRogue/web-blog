package ru.yandex.practicum.blog.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;

@Configuration
@Profile("unit-test")
public class DaoMockConfig {

    @Bean
    public CommentDao commentDao() {
        return Mockito.mock(CommentDao.class);
    }

    @Bean
    public PostDao postDao() {
        return Mockito.mock(PostDao.class);
    }

}
