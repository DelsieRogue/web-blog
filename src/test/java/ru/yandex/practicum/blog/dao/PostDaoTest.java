package ru.yandex.practicum.blog.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.config.DaoBeanConfig;
import ru.yandex.practicum.blog.config.JdbcMockConfig;
import ru.yandex.practicum.blog.dao.mapper.PostPreviewMapper;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("unit-test")
@SpringJUnitConfig(classes = {JdbcMockConfig.class, DaoBeanConfig.class})
class PostDaoTest {
    @Autowired
    private PostDao postDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        Mockito.reset(jdbcTemplate);
    }

    @Test
    void getPostPreviewList_shouldReturnPostPreviews() {
        List<PostPreviewDto> expectedResult = List.of(new PostPreviewDto().setId(1L));
        when(jdbcTemplate.query(anyString(), any(PostPreviewMapper.class), any(), any(), any()))
                .thenReturn(expectedResult);
        List<PostPreviewDto> result = postDao.getPostPreviewList(1, 10, "tag");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(jdbcTemplate, times(1)).query(anyString(), any(PostPreviewMapper.class), eq("%tag%"), eq(10), eq(0));
    }

    @Test
    void getCount_shouldReturnCount() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), any()))
                .thenReturn(10L);
        Long result = postDao.getCount("tag");
        assertEquals(10L, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class), eq("%tag%"));
    }

    @Test
    void getPostById_shouldReturnPost() {
        Post expectedPost = new Post().setId(1L);
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), any()))
                .thenReturn(expectedPost);
        Post result = postDao.getPostById(1L);
        assertNotNull(result);
        assertEquals(expectedPost.getId(), result.getId());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(1L));
    }

    @Test
    void createPost_shouldExecuteUpdate() {
        Post post = new Post().setId(1L).setTitle("Title").setContent("Content")
                .setImageName("image.png").setTags("tags");
        postDao.createPost(post);
        verify(jdbcTemplate, times(1)).update(anyString(), eq("Title"),
                eq("image.png"), eq("Content"), eq("tags"));
    }

    @Test
    void updatePost_shouldExecutePreparedStatement() throws Exception {
        postDao.updatePost(1L, new Post());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class));
    }

    @Test
    void deletePost_shouldExecuteUpdate() {
        postDao.deletePost(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }

    @Test
    void addLike_shouldReturnUpdatedLikeCount() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), any()))
                .thenReturn(15L);
        Long result = postDao.addLike(1L);
        assertEquals(15L, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class), eq(1L));
    }
}
