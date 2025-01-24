package ru.yandex.practicum.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PostDto {
    private String title;
    private String imageName;
    private Boolean isNeedDeleteImage;
    private String content;
    private String tags;
}
