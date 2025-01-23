package ru.yandex.practicum.blog.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Post {
    private Long id;
    private String title;
    private String imageName;
    private String content;
    private String tags;
    private Long likeCount;
    private LocalDateTime createdAt;
}
