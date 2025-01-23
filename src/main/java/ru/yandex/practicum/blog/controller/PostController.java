package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.service.PostService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    public String getPostPreviewList(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String tagFilter) {
        List<PostPreviewDto> posts = postService.getPostPreviewList(page, size, tagFilter);
        Long totalPosts = postService.getCountPost(tagFilter);
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        model.addAttribute("posts", posts);
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("filter", tagFilter);
        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostView(Model model, @PathVariable("id") Long postId){
        PostViewDto postView = postService.getPostViewById(postId);
        model.addAttribute("post", postView);
        return "post";
    }
}
