package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.utils.ContentUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostDao postDao;
    private final CommentDao commentDao;

    private final ImageService imageService;

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
                .setImageName(post.getImageName())
                .setComments(commentDtoList);
    }

    public void createPost(Post post, MultipartFile image) {
        Optional<String> fileName = imageService.save(image);
        fileName.ifPresent(post::setImageName);
        postDao.createPost(post);
    }

    public void updatePost(Post post, MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.deleteImage(post.getImageName());
            Optional<String> fileName = imageService.save(image);
            fileName.ifPresent(post::setImageName);
        }
        postDao.updatePost(post);
    }

    public void deletePost(Long postId) {
        Post post = postDao.getPostById(postId);
        if (post.getImageName() != null) {
            imageService.deleteImage(post.getImageName());
        }
        postDao.deletePost(postId);
    }

    public Long addLike(Long postId) {
        return postDao.addLike(postId);
    }
}
