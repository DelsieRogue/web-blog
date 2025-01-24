package ru.yandex.practicum.blog.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentUtils {
    private static final Integer PREVIEW_CONTENT_LENGTH = 1000;
    private static final String SKIP_SYMBOL_REGEX = "[\\n\\t]";

    public static String getPreviewContent(String content) {
        String formattedContent = content.replaceAll(SKIP_SYMBOL_REGEX, "");
        return formattedContent.substring(0, Math.min(formattedContent.length(), PREVIEW_CONTENT_LENGTH));
    }

    public static List<String> getContentByLine(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }
        return Stream.of(content.split("\n")).collect(Collectors.toList());
    }

    public static String getContentFromLine(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return null;
        }
        return String.join("\n", lines);
    }
}
