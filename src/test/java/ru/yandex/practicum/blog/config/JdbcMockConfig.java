package ru.yandex.practicum.blog.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Profile("unit-test")
public class JdbcMockConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return Mockito.mock(JdbcTemplate.class);
    }
}
