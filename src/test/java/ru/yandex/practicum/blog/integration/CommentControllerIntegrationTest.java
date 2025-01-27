package ru.yandex.practicum.blog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.blog.config.JdbcBeanConfiguration;
import ru.yandex.practicum.blog.config.WebConfiguration;
import ru.yandex.practicum.blog.dto.CommentDto;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@WebAppConfiguration
@SpringJUnitConfig(classes = {WebConfiguration.class, JdbcBeanConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_name, content, tags)" +
                " VALUES (1, 'Название', 'img.png', 'содержание поста', 'tag')");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content) VALUES (1, 1, 'содержание коментария')");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content) VALUES (2, 1, 'содержание коментария2')");
        jdbcTemplate.update("ALTER TABLE post ALTER COLUMN id RESTART WITH 2");
        jdbcTemplate.update("ALTER TABLE comment ALTER COLUMN id RESTART WITH 3");
    }

    @Test
    public void createComment() throws Exception {
        CommentDto commentDto = new CommentDto().setContent("comment1").setPostId(1L);
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("comment1")));
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
