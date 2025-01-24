package ru.yandex.practicum.blog.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.blog.dto.PostDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.utils.ContentUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

    public static Post fromPostDto(PostDto postDto) {
        return new Post()
                .setTags(postDto.getTags())
                .setContent(postDto.getContent())
                .setTitle(postDto.getTitle())
                .setImageName(postDto.getImageName());
    }

    public static PostDto fromPostViewDto(PostViewDto postViewDto) {
        return new PostDto()
                .setContent(ContentUtils.getContentFromLine(postViewDto.getContent()))
                .setTags(postViewDto.getTags())
                .setTitle(postViewDto.getTitle())
                .setImageName(postViewDto.getImageName());
    }
}
