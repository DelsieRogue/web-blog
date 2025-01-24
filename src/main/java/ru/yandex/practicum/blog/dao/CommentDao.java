package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.mapper.CommentDtoMapper;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String COMMENTS_BY_POST_SELECT_TEMPLATE = """
            SELECT c.id AS id,
                   c.content AS content
            FROM comment c WHERE c.post_id = ?
            ORDER BY created_at;
            """;
    private static final String COMMENT_BY_ID_UPDATE_TEMPLATE = """
            UPDATE comment SET content = ? WHERE id = ?
            """;
    private static final String COMMENT_CREATE_TEMPLATE = """
            INSERT INTO comment (content, post_id) VALUES (?, ?) RETURNING id, content;
            """;

    private static final String COMMENT_DELETE_TEMPLATE = """
            DELETE from comment WHERE id = ?
            """;

    public List<CommentDto> getByPostId(Long postId) {
        return jdbcTemplate.query(COMMENTS_BY_POST_SELECT_TEMPLATE, new CommentDtoMapper(), postId);
    }

    public void updateComment(Long id, Comment comment) {
        jdbcTemplate.update(COMMENT_BY_ID_UPDATE_TEMPLATE, comment.getContent(), id);
    }

    public CommentDto createComment(Comment comment) {
        return jdbcTemplate.queryForObject(COMMENT_CREATE_TEMPLATE, new CommentDtoMapper(),
                comment.getContent(), comment.getPostId());
    }

    public void deleteComment(Long commentId) {
        jdbcTemplate.update(COMMENT_DELETE_TEMPLATE, commentId);
    }
}
