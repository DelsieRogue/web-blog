package ru.yandex.practicum.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Comment {
    private Long id;
    private String content;
    private Long postId;
    private LocalDateTime createdAt;
}
