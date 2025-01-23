package ru.yandex.practicum.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PostPreviewDto {
    private Long id;
    private String title;
    private String imagePath;
    private String contentPreview;
    private String tags;
    private Long likeCount;
    private Long commentCount;
}
