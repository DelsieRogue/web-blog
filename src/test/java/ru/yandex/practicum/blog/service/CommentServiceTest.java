package ru.yandex.practicum.blog.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CommentService.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockitoBean
    private CommentDao commentDao;

    @Test
    void updateComment() {
        CommentDto commentDto = new CommentDto()
                .setContent("content").setPostId(2L);
        ArgumentCaptor<Comment> argumentCaptor = ArgumentCaptor.forClass(Comment.class);
        commentService.updateComment(1L, commentDto);
        verify(commentDao, times(1)).updateComment(eq(1L), argumentCaptor.capture());
        Comment actual = argumentCaptor.getValue();
        assertEquals(commentDto.getContent(), actual.getContent());
        assertEquals(commentDto.getPostId(), actual.getPostId());
    }

    @Test
    void createComment() {
        CommentDto commentDto = new CommentDto()
                .setContent("content2").setPostId(3L);
        commentService.createComment(commentDto);
        ArgumentCaptor<Comment> argumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDao, times(1)).createComment(argumentCaptor.capture());
        Comment actual = argumentCaptor.getValue();
        assertEquals(commentDto.getContent(), actual.getContent());
        assertEquals(commentDto.getPostId(), actual.getPostId());
    }

    @Test
    void deleteComment() {
        commentService.deleteComment(1L);
        verify(commentDao, times(1)).deleteComment(1L);
    }
}