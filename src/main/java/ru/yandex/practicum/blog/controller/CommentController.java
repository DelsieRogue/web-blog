package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.service.CommentService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/edit")
    @ResponseBody
    public void updateComment(@PathVariable("id") Long commentId, @RequestBody CommentDto commentDto) {
        commentService.updateComment(commentId, commentDto);
    }

    @PostMapping
    @ResponseBody
    public CommentViewDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
    }
}
