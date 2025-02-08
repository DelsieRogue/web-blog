package ru.yandex.practicum.blog.integration.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;

@JdbcTest
@Import(CommentDao.class)
class CommentDaoIntegrationTest {
    @Autowired
    CommentDao commentDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("""
                DELETE FROM post;
                DELETE FROM comment;
                """);
        jdbcTemplate.execute("""
                INSERT INTO post (id , title, image_name, content, tags, like_count)
                VALUES (1, 'Название статьи №1', '100.png', 'Описание', '#tag', 4);
                """);
    }


    @Test
    void getByPostId() {
        List<String> contents = List.of("HELLO", "HELLO2");
        contents.forEach(s -> commentDao.createComment(new Comment().setPostId(1L).setContent(s)));

        List<CommentViewDto> commentViewDtos = commentDao.getByPostId(1L);
        Assertions.assertAll(() -> Assertions.assertEquals(2, commentViewDtos.size()),
                () -> Assertions.assertTrue(contents.contains(commentViewDtos.getFirst().getContent())),
                () -> Assertions.assertTrue(contents.contains(commentViewDtos.get(1).getContent())));
    }

    @Test
    void updateComment() {
        CommentViewDto commentView = commentDao.createComment(new Comment().setPostId(1L).setContent("test"));
        commentDao.updateComment(commentView.getId(), new Comment().setContent("new"));
        Comment comment = getCommentById(commentView.getId());
        Assertions.assertAll(() -> Assertions.assertEquals(commentView.getId(), comment.getId()),
                () -> Assertions.assertEquals("new", comment.getContent()));
    }

    @Test
    void createComment() {
        CommentViewDto commentView = commentDao.createComment(new Comment().setPostId(1L).setContent("test"));
        Comment comment = getCommentById(commentView.getId());
        Assertions.assertAll(() -> Assertions.assertEquals(1, comment.getPostId()),
                () -> Assertions.assertEquals("test", comment.getContent()));
    }


    @Test
    void deleteComment() {
        CommentViewDto commentView = commentDao.createComment(new Comment().setPostId(1L).setContent("test"));
        commentDao.deleteComment(commentView.getId());
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> getCommentById(commentView.getId()));
    }

    private Comment getCommentById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM comment WHERE id = ?",
                new BeanPropertyRowMapper<>(Comment.class), id);
    }

}