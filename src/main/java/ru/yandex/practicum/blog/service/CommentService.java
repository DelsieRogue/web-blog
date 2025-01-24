package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.mapper.CommentMapper;
import ru.yandex.practicum.blog.model.Comment;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentDao commentDao;

    public void updateComment(Long id, CommentDto commentDto) {
        Comment comment = CommentMapper.fromCommentDto(commentDto);
        commentDao.updateComment(id, comment);
    }

    public CommentViewDto createComment(CommentDto commentDto) {
        Comment comment = CommentMapper.fromCommentDto(commentDto);
        return commentDao.createComment(comment);
    }

    public void deleteComment(Long commentId) {
        commentDao.deleteComment(commentId);
    }
}
