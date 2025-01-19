package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostDao {
    private final JdbcTemplate jdbcTemplate;

}
