WITH content AS (SELECT 'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' ||
                        'Описание было очень большим, описание было очень большим. ' AS content_text)
INSERT INTO post (title, image_name, content, tags, like_count)
VALUES
            ('Название статьи №1', '100.png', (select content_text from content), '#tag', 4),
            ('Название статьи №2', '200.jpg', (select content_text from content), '#java', 10),
            ('Название статьи №3', '300.jpeg', (select content_text from content), '#sport', 2),
            ('Название статьи №4', '400.jpeg', (select content_text from content), '#sport', 0),
            ('Название статьи №5', '500.png', (select content_text from content), '#sport', 12),
            ('Название статьи №6', '600.jpeg', (select content_text from content), '#java', 3),
            ('Название статьи №7', '700.png', (select content_text from content), '#sport', 67),
            ('Название статьи №8', '800.jpeg', (select content_text from content), '#java', 24),
            ('Название статьи №9', '900.png', (select content_text from content), '#sport', 23)
 ON CONFLICT (id) DO NOTHING;

