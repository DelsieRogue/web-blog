package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.model.Comment;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentDao commentDao;

    public void updateComment(Long id, Comment comment) {
        commentDao.updateComment(id, comment);
    }

    public CommentDto createComment(Comment comment) {
        return commentDao.createComment(comment);
    }

    public void deleteComment(Long commentId) {
        commentDao.deleteComment(commentId);
    }
}
