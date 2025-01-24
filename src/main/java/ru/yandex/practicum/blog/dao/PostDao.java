package ru.yandex.practicum.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dao.mapper.PostPreviewMapper;
import ru.yandex.practicum.blog.model.Post;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String PREVIEW_POSTS_SELECT_TEMPLATE = """
            SELECT p.id AS id,
                   p.title AS title,
                   p.content AS content,
                   p.image_name AS image_name,
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
                   p.image_name AS image_name,
                   p.created_at AS created_at
            FROM post p WHERE p.id = ?;
            """;

    private static final String POST_CREATE_TEMPLATE = """
            INSERT INTO post (title, image_name, content, tags) VALUES (?, ?, ?, ?);
            """;

    private static final String POST_UPDATE_TEMPLATE = """
            UPDATE post SET title = ?, image_name = ?, content = ?, tags = ? 
            WHERE id = ?
            """;

    private static final String POST_DELETE_TEMPLATE = """
            DELETE from post WHERE id = ?
            """;

    private static final String POST_ADD_LIKE_TEMPLATE = """
            UPDATE post
            SET like_count = like_count + 1
            WHERE id = ? RETURNING like_count;
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
        jdbcTemplate.update(POST_CREATE_TEMPLATE, post.getTitle(), post.getImageName(), post.getContent(), post.getTags());
    }

    public void updatePost(Long postId, Post post) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(POST_UPDATE_TEMPLATE);
            ps.setString(1, post.getTitle());
            if (post.getImageName() == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, post.getImageName());
            }
            ps.setString(3, post.getContent());
            ps.setString(4, post.getTags());
            ps.setLong(5, postId);
            return ps;
        });
    }

    public void deletePost(Long postId) {
        jdbcTemplate.update(POST_DELETE_TEMPLATE, postId);
    }

    public Long addLike(Long postId) {
        return jdbcTemplate.queryForObject(POST_ADD_LIKE_TEMPLATE, Long.class, postId);
    }
}
