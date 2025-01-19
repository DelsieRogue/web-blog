package ru.yandex.practicum.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CommentDto {
    private Long commentId;
    private String content;
}
