package ru.yandex.practicum.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PostPreviewDto {
    private Long id;
    private String title;
    private String image;
    private List<String> contentPreview;
    private String tags;
    private Long likeCount;
    private Long commentCount;
}
