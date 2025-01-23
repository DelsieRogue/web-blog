package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.mapper.CommentDtoMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String COMMENTS_BY_POST_SELECT_TEMPLATE = """
            SELECT c.id AS id,
                   c.content AS content
            FROM comment c WHERE c.post_id = ?
            ORDER BY created_at DESC;
            """;

    public List<CommentDto> getByPostId(Long postId) {
        return jdbcTemplate.query(COMMENTS_BY_POST_SELECT_TEMPLATE, new CommentDtoMapper(), postId);
    }
}
