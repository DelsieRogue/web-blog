package ru.yandex.practicum.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;

@Import(JdbcMockConfig.class)
@Profile("unit-test")
@Configuration
public class DaoBeanConfig {
    @Bean
    public CommentDao commentDao(JdbcTemplate jdbcTemplate) {
        return new CommentDao(jdbcTemplate);
    }

    @Bean
    public PostDao postDao(JdbcTemplate jdbcTemplate) {
        return new PostDao(jdbcTemplate);
    }
}
