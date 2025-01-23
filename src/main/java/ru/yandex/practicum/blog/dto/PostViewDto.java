package ru.yandex.practicum.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PostViewDto {
    private Long id;
    private String title;
    private String imagePath;
    private List<String> content;
    private String tags;
    private Long likeCount;
    private List<CommentDto> comments;
}
