package ru.yandex.practicum.blog.integration.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.blog.dao.PostDao;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import(PostDao.class)
class PostDaoIntegrationTest {
    @Autowired
    private PostDao postDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("""
                DELETE FROM post;
                DELETE FROM comment;
                """);
    }

    @Test
    void getPostPreviewList() {
        List<Post> posts = List.of(new Post().setTitle("1t").setContent("1c").setImageName("1img").setTags("#java"),
                new Post().setTitle("2t").setContent("2c").setImageName("2img").setTags("#java"),
                new Post().setTitle("3t").setContent("3c").setImageName("3img").setTags("#sql"),
                new Post().setTitle("4t").setContent("4c").setImageName("4img").setTags("#java"),
                new Post().setTitle("5t").setContent("5c").setImageName("5img").setTags("#go"),
                new Post().setTitle("6t").setContent("6c").setImageName("6img").setTags("#java"),
                new Post().setTitle("7t").setContent("7c").setImageName("7img").setTags("#go"));
        posts.forEach(p -> postDao.createPost(p));

        List<PostPreviewDto> postPreviewList = postDao.getPostPreviewList(2, 2, "#java");
        Assertions.assertAll(() -> Assertions.assertEquals(2, postPreviewList.size()),
                () -> Assertions.assertTrue(postPreviewList.stream().allMatch(s -> "#java".equals(s.getTags()))));
    }

    @Test
    void getCount() {
        List<Post> posts = List.of(new Post().setTitle("1t").setContent("1c").setImageName("1img").setTags("#java"),
                new Post().setTitle("2t").setContent("2c").setImageName("2img").setTags("#java"),
                new Post().setTitle("3t").setContent("3c").setImageName("3img").setTags("#sql"));
        posts.forEach(p -> postDao.createPost(p));
        Long count = postDao.getCount("#java");
        Assertions.assertEquals(2L, count);
    }

    @Test
    void getPostById() {
        Post newPost = new Post().setTitle("Название статьи №1").setContent("Контент")
                .setImageName("100.png").setTags("#tag");
        Post post = postDao.createPost(newPost);

        Assertions.assertAll(() -> Assertions.assertEquals("Контент", post.getContent()),
                () -> Assertions.assertEquals("Название статьи №1", post.getTitle()),
                () -> Assertions.assertEquals("#tag", post.getTags()),
                () -> Assertions.assertEquals("100.png", post.getImageName()));
    }

    @Test
    void createPost() {
        Post newPost = new Post().setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        Post post = postDao.createPost(newPost);
        Assertions.assertAll(() -> Assertions.assertEquals(newPost.getTitle(), post.getTitle()),
                () -> Assertions.assertEquals(newPost.getContent(), post.getContent()),
                () -> Assertions.assertEquals(newPost.getImageName(), post.getImageName()),
                () -> Assertions.assertEquals(newPost.getTags(), post.getTags()));
    }

    @Test
    void updatePost() {
        Post newPost = new Post().setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        Long id = postDao.createPost(newPost).getId();

        Post updatePost = new Post().setTitle("Title2").setContent("Content2")
                .setImageName("image2.png").setTags("tags2");
        postDao.updatePost(id, updatePost);
        Post updatedPost = postDao.getPostById(id);
        Assertions.assertAll(() -> Assertions.assertEquals(updatePost.getTitle(), updatedPost.getTitle()),
                () -> Assertions.assertEquals(updatePost.getContent(), updatedPost.getContent()),
                () -> Assertions.assertEquals(updatePost.getImageName(), updatedPost.getImageName()),
                () -> Assertions.assertEquals(updatePost.getTags(), updatedPost.getTags()));
    }

    @Test
    void deletePost() {
        Post newPost = new Post().setId(1L).setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        Long id = postDao.createPost(newPost).getId();
        postDao.deletePost(id);
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> postDao.getPostById(id));
    }

    @Test
    void addLike() {
        Post newPost = new Post().setId(1L).setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        Long id = postDao.createPost(newPost).getId();
        assertAll(() -> Assertions.assertEquals(1, postDao.addLike(id)),
                () -> Assertions.assertEquals(2, postDao.addLike(id)),
                () -> Assertions.assertEquals(3, postDao.addLike(id)),
                () -> Assertions.assertEquals(4, postDao.addLike(id)));
    }

}
