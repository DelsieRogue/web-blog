package ru.yandex.practicum.blog.integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.blog.controller.CommentController;
import ru.yandex.practicum.blog.dto.CommentDto;
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.service.CommentService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CommentService commentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createComment() throws Exception {
        CommentDto commentDto = new CommentDto().setContent("Hello").setPostId(1L);
        CommentViewDto commentViewDto = new CommentViewDto().setContent("Hello").setId(1L);
        when(commentService.createComment(any())).thenReturn(commentViewDto);
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello")));
    }

    @Test
    public void updateComment() throws Exception {
        mockMvc.perform(put("/comment/{id}/edit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"обновленный комментарий\"}"))
                .andExpect(status().isOk());
    }


    @Test
    public void deleteComment() throws Exception {
        mockMvc.perform(delete("/comment/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
