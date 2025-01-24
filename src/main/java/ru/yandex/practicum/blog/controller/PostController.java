package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.dto.PostDto;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.dto.PostViewDto;
import ru.yandex.practicum.blog.mapper.PostMapper;
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
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("filter", tagFilter);
        model.addAttribute("newPost", new PostDto());
        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostView(Model model, @PathVariable("id") Long postId) {
        PostViewDto postView = postService.getPostViewById(postId);
        model.addAttribute("post", postView);
        model.addAttribute("editPost", PostMapper.fromPostViewDto(postView));
        return "post";
    }

    @PutMapping("/{id}/like")
    @ResponseBody
    public Long addLike(@PathVariable("id") Long postId) {
        return postService.addLike(postId);
    }

    @PostMapping
    public String createPost(@ModelAttribute("newPost") PostDto postDto, @RequestPart(value = "image", required = false) MultipartFile image) {
        postService.createPost(postDto, image);
        return "redirect:/post";
    }

    @PutMapping("/{id}/edit")
    public String updatePost(@PathVariable("id") Long postId,
                             @ModelAttribute("post") PostDto editDto,
                             @RequestPart(value = "image", required = false) MultipartFile image) {
        postService.updatePost(postId, editDto, image);
        return "redirect:/post/" + postId;
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return "redirect:/post";
    }
}
