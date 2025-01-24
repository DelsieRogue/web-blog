package ru.yandex.practicum.blog.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment fromCommentDto(CommentDto commentDto) {
        return new Comment()
                .setContent(commentDto.getContent())
                .setPostId(commentDto.getPostId());
    }
}
