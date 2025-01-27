package ru.yandex.practicum.blog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Profile("integration-test")
@ComponentScan(basePackages = {"ru.yandex.practicum.blog"})
public class WebConfiguration {

}
