package ru.yandex.practicum.blog.dao;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.blog.dao.mapper.CommentDtoMapper;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CommentDao.class)
class CommentDaoTest {
    @Autowired
    CommentDao commentDao;
    @MockitoBean
    JdbcTemplate jdbcTemplate;

    @Test
    void getByPostId() {
        CommentViewDto commentViewDto = new CommentViewDto().setContent("test");
        List<CommentViewDto> expectedComments = List.of(commentViewDto);
        when(jdbcTemplate.query(anyString(), any(CommentDtoMapper.class), eq(1L))).thenReturn(expectedComments);
        List<CommentViewDto> actualComments = commentDao.getByPostId(1L);

        assertNotNull(actualComments);
        assertEquals(expectedComments.size(), actualComments.size());
        assertEquals(expectedComments.get(0), actualComments.get(0));
        verify(jdbcTemplate, times(1))
                .query(anyString(), any(CommentDtoMapper.class), eq(1L));
    }

    @Test
    void updateComment() {
        commentDao.updateComment(1L, new Comment().setContent("test"));
        ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(jdbcTemplate, times(1))
                .update(anyString(), argumentCaptor.capture());
        Object[] capturedArgs = argumentCaptor.getValue();

        assertEquals(2, capturedArgs.length);
        assertEquals("test", capturedArgs[0]);
        assertEquals(1L, capturedArgs[1]);
    }

    @Test
    void createComment() {
        Comment comment = new Comment();
        comment.setContent("comment");
        comment.setPostId(1L);
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenAnswer(invocation -> {
                    GeneratedKeyHolder keyHolder = invocation.getArgument(1);
                    keyHolder.getKeyList().add(Map.of("id", 1));
                    return 1;
                });
        CommentViewDto result = commentDao.createComment(comment);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("comment", result.getContent());
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class),
                any(KeyHolder.class));
    }


    @Test
    void deleteComment() {
        commentDao.deleteComment(1L);
        verify(jdbcTemplate, times(1))
                .update(anyString(), eq(1L));
    }
}