package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dao.CommentDao;
import ru.yandex.practicum.blog.dao.PostDao;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostDao postDao;
    private final CommentDao commentDao;

}
