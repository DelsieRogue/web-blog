package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.model.Post;
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
        model.addAttribute("newPost", new Post());
        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostView(Model model, @PathVariable("id") Long postId){
        PostViewDto postView = postService.getPostViewById(postId);
        model.addAttribute("post", postView);
        return "post";
    }

    @PostMapping
    public String createPost(@ModelAttribute("newPost") Post post, @RequestPart(value = "image", required = false) MultipartFile image) {
        postService.createPost(post, image);
        return "redirect:/post";
    }

//    @PutMapping("/{id}")
//    public String updatePost(@PathVariable("id") Long postId,
//                             @ModelAttribute("post") Post post,
//                             @RequestPart(value = "image", required = false) MultipartFile image) {
//        postService.createPost(post, image);
//        return "redirect:/post/" + postId;
//    }
}
