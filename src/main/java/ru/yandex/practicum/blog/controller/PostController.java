package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.blog.service.PostService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    public String test(Model model) {
        model.addAttribute("test", "pong");
        return "post";
    }
}
