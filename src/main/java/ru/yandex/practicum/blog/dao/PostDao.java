package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.mapper.PostPreviewMapper;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String PREVIEW_POSTS_SELECT_TEMPLATE = """
            SELECT p.id AS id,
                   p.title AS title,
                   p.content AS content,
                   p.image_path AS image_path,
                   p.tags AS tags,
                   p.like_count AS like_count,
                   count(c.id) AS comment_count
            FROM post p
            LEFT JOIN comment c ON p.id = c.post_id
            WHERE p.tags LIKE ?
            GROUP BY p.id, p.created_at
            ORDER BY p.created_at DESC
            LIMIT ?
            OFFSET ?;
            """;
    private static final String PREVIEW_POSTS_COUNT_TEMPLATE = """
            SELECT count(*) from post where tags LIKE ?;
            """;

    private static final String VIEW_POST_SELECT_TEMPLATE = """
            SELECT p.id AS id,
                   p.title AS title,
                   p.content AS content,
                   p.tags AS tags,
                   p.like_count AS like_count,
                   p.image_path AS image_path,
                   p.created_at AS created_at
            FROM post p WHERE p.id = ?;
            """;

    private static final String POST_INSERT_TEMPLATE = """
            INSERT INTO post (title, image_path, content, tags) VALUES (?, ?, ?, ?);
            """;

    public List<PostPreviewDto> getPostPreviewList(Integer page, Integer size, String tagFilter) {
        String filter = getTagFilter(tagFilter);
        return jdbcTemplate.query(PREVIEW_POSTS_SELECT_TEMPLATE, new PostPreviewMapper(),
                filter, size, (page - 1) * size);
    }

    public Long getCount(String tagFilter) {
        String filter = getTagFilter(tagFilter);
        return jdbcTemplate.queryForObject(PREVIEW_POSTS_COUNT_TEMPLATE, Long.class, filter);
    }

    public Post getPostById(Long postId) {
        return jdbcTemplate.queryForObject(VIEW_POST_SELECT_TEMPLATE, new BeanPropertyRowMapper<>(Post.class), postId);
    }

    private String getTagFilter(String tagFilter) {
        return tagFilter != null && !tagFilter.isBlank() ? "%" + tagFilter + "%" : "%";
    }

    public void createPost(Post post) {
        jdbcTemplate.update(POST_INSERT_TEMPLATE, post.getTitle(), post.getImagePath(), post.getContent(), post.getTags());
    }
}
