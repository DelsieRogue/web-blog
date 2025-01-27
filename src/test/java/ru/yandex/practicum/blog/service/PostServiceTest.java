package ru.yandex.practicum.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.config.DaoMockConfig;
import ru.yandex.practicum.blog.config.ServiceMockConfig;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.dto.PostDto;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("unit-test")
@SpringJUnitConfig(PostServiceTest.PostServiceTestConfig.class)
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ImageService imageService;


    @BeforeEach
    void beforeEach() {
        Mockito.clearInvocations(commentDao, postDao, imageService);
    }

    @Test
    void getPostPreviewList() {
        postService.getPostPreviewList(1, 2, "3");
        verify(postDao, times(1)).getPostPreviewList(eq(1), eq(2), eq("3"));
    }

    @Test
    void getCountPost() {
        postService.getCountPost("1");
        verify(postDao, times(1)).getCount(eq("1"));
    }

    @Test
    void getPostViewById() {
        Post post = new Post().setId(1L);
        CommentViewDto comment = new CommentViewDto().setId(2L).setContent("comment");
        when(commentDao.getByPostId(3L)).thenReturn(List.of(comment));
        when(postDao.getPostById(eq(1L))).thenReturn(post);
        postService.getPostViewById(1L);
        verify(postDao, times(1)).getPostById(eq(1L));
        verify(commentDao, times(1)).getByPostId(eq(1L));
    }

    @Test
    void createPost() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        postService.createPost(new PostDto(), multipartFile);
        verify(postDao, times(1)).createPost(any());
        verify(imageService, times(1)).save(multipartFile);
    }

    @Test
    void updatePost_withoutImg() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(Boolean.TRUE);
        postService.updatePost(1L, new PostDto().setIsNeedDeleteImage(false), multipartFile);
        verify(postDao, times(1)).updatePost(any(), any());
        verify(imageService, times(0)).deleteImage(anyString());
    }

    @Test
    void updatePost_withImg() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(Boolean.FALSE);
        postService.updatePost(1L, new PostDto().setIsNeedDeleteImage(true), multipartFile);
        verify(postDao, times(1)).updatePost(any(), any());
        verify(imageService, times(1)).save(any());
    }

    @Test
    void deletePost() {
        when(postDao.getPostById(1L)).thenReturn(new Post().setImageName("not_null.png"));
        postService.deletePost(1L);
        verify(postDao, times(1)).deletePost(any());
        verify(imageService, times(1)).deleteImage(any());
    }

    @Test
    void addLike() {
        when(postDao.addLike(eq(1L))).thenReturn(2L);
        Long count = postService.addLike(1L);
        verify(postDao, times(1)).addLike(eq(1L));
        assertEquals(2L, count);
    }

    @Import({DaoMockConfig.class, ServiceMockConfig.class})
    @Configuration
    static class PostServiceTestConfig {
        @Primary
        @Bean
        public PostService postService(PostDao postDao, CommentDao commentDao, ImageService imageService) {
            return new PostService(postDao, commentDao, imageService);
        }
    }
}