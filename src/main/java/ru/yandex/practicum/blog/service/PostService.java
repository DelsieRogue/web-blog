package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.utils.ContentUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostDao postDao;
    private final CommentDao commentDao;

    public List<PostPreviewDto> getPostPreviewList(Integer page, Integer size, String tagFilter) {
        return postDao.getPostPreviewList(page, size, tagFilter);
    }
    public Long getCountPost(String tagFilter) {
        return postDao.getCount(tagFilter);
    }

    public PostViewDto getPostViewById(Long postId) {
        Post post = postDao.getPostById(postId);
        List<CommentDto> commentDtoList = commentDao.getByPostId(postId);
        return new PostViewDto()
                .setId(post.getId())
                .setTitle(post.getTitle())
                .setContent(ContentUtils.getContentByLine(post.getContent()))
                .setLikeCount(post.getLikeCount())
                .setTags(post.getTags())
                .setImagePath(post.getImagePath())
                .setComments(commentDtoList);
    }
}
