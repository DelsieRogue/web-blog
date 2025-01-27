package ru.yandex.practicum.blog.integration;

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
import ru.yandex.practicum.blog.dto.CommentViewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("integration-test")
@WebAppConfiguration
@SpringJUnitConfig(classes = {WebConfiguration.class, JdbcBeanConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MockMvc mockMvc;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_name, content, tags)" +
                " VALUES (1, 'Название', 'img.png', 'содержание поста', 'tag')");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_name, content, tags) " +
                "VALUES (2, 'Название2', 'img2.png', 'содержание поста2', 'tag')");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content) VALUES (1, 1, 'содержание коментария')");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content) VALUES (2, 2, 'содержание коментария2')");
        jdbcTemplate.update("ALTER TABLE post ALTER COLUMN id RESTART WITH 3");
        jdbcTemplate.update("ALTER TABLE comment ALTER COLUMN id RESTART WITH 3");
    }

    @Test
    public void getPostPreviewList() throws Exception {
        assertNotNull(applicationContext.getBean(JdbcTemplate.class), "JdbcTemplate bean is not loaded");

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
        mockMvc.perform(get("/post/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", postViewDto));
    }

    @Test
    public void addLike() throws Exception {
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
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/post/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post"));
    }

}

