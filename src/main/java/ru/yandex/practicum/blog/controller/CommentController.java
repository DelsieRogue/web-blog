package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.service.CommentService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/edit")
    @ResponseBody
    public void updateComment(@PathVariable("id") Long commentId, @RequestBody Comment comment) {
        commentService.updateComment(commentId, comment);
    }

    @PostMapping
    @ResponseBody
    public CommentDto createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
    }
}
