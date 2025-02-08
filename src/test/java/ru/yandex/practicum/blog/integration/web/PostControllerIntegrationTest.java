package ru.yandex.practicum.blog.integration.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.blog.controller.PostController;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(PostController.class)
public class PostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PostService postService;

    @Test
    public void getPostPreviewList() throws Exception {
        List<PostPreviewDto> postPreviewDtos = List.of(new PostPreviewDto().setTitle("1").setContentPreview("2").setId(1L)
                .setTags("#1").setLikeCount(2L).setImageName("name.jpg"));
        when(postService.getPostPreviewList(any(), any(), anyString())).thenReturn(postPreviewDtos);

        mockMvc.perform(get("/post").param("tagFilter", "tag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts", "totalPages", "currentPage", "pageSize", "filter", "newPost"));
    }

    @Test
    public void getPostView() throws Exception {
        PostViewDto postViewDto = new PostViewDto()
                .setId(1L)
                .setTitle("Название")
                .setImageName("img.png")
                .setContent(List.of("содержание поста"))
                .setTags("tag")
                .setLikeCount(0L)
                .setComments(List.of(new CommentViewDto().setContent("содержание коментария").setId(1L)));
        when(postService.getPostViewById(any())).thenReturn(postViewDto);
        mockMvc.perform(get("/post/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", postViewDto));
    }

    @Test
    public void addLike() throws Exception {
        when(postService.addLike(any())).thenReturn(1L);
        mockMvc.perform(put("/post/{id}/like", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void createPost() throws Exception {
        mockMvc.perform(multipart("/post")
                        .file("image", new byte[0])
                        .param("title", "test title222")
                        .param("content", "test content222")
                        .param("tags", "tags222")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
        verify(postService, times(1)).createPost(any(), any());
    }

    @Test
    void updatePost() throws Exception {
        mockMvc.perform(multipart("/post/{id}/edit", 1)
                        .file("image", new byte[0])
                        .param("title", "editTitle")
                        .param("content", "editContent")
                        .param("tags", "editTags")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + 1));
        verify(postService, times(1)).updatePost(any(), any(), any());
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/post/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
        verify(postService, times(1)).deletePost(any());
    }
}
