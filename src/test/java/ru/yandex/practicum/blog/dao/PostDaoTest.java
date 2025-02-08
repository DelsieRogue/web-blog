package ru.yandex.practicum.blog.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.dao.mapper.PostPreviewMapper;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {PostDao.class})
class PostDaoTest {
    @Autowired
    private PostDao postDao;
    @MockitoBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void getPostPreviewList() {
        List<PostPreviewDto> expectedResult = List.of(new PostPreviewDto().setId(1L));
        when(jdbcTemplate.query(anyString(), any(PostPreviewMapper.class), any(), any(), any()))
                .thenReturn(expectedResult);
        List<PostPreviewDto> result = postDao.getPostPreviewList(1, 10, "tag");

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        verify(jdbcTemplate, times(1)).query(anyString(), any(PostPreviewMapper.class), eq("%tag%"), eq(10), eq(0));
    }

    @Test
    void getCount() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), any()))
                .thenReturn(10L);
        Long result = postDao.getCount("tag");
        assertEquals(10L, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class), eq("%tag%"));
    }

    @Test
    void getPostById() {
        Post expectedPost = new Post().setId(1L);
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), any()))
                .thenReturn(expectedPost);
        Post result = postDao.getPostById(1L);
        assertNotNull(result);
        assertEquals(expectedPost.getId(), result.getId());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(1L));
    }

    @Test
    void createPost() {
        Post newPost = new Post().setId(1L).setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenAnswer(invocation -> {
                    GeneratedKeyHolder keyHolder = invocation.getArgument(1);
                    keyHolder.getKeyList().add(Map.of("id", 1));
                    return 1;
                });
        Post createdPost = postDao.createPost(newPost);
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
        assertEquals(1, createdPost.getId());
    }

    @Test
    void updatePost() throws Exception {
        postDao.updatePost(1L, new Post());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class));
    }

    @Test
    void deletePost() {
        postDao.deletePost(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }

    @Test
    void addLike() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), any()))
                .thenReturn(15L);
        Long result = postDao.addLike(1L);
        assertEquals(15L, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class), eq(1L));
    }
}
