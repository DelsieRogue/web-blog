package ru.yandex.practicum.blog.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.blog.dto.PostPreviewDto;
import ru.yandex.practicum.blog.utils.ContentUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostPreviewMapper implements RowMapper<PostPreviewDto> {
    @Override
    public PostPreviewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PostPreviewDto()
                .setId(rs.getLong("id"))
                .setTitle(rs.getString("title"))
                .setImageName(rs.getString("image_name"))
                .setTags(rs.getString("tags"))
                .setLikeCount(rs.getLong("like_count"))
                .setContentPreview(ContentUtils.getPreviewContent(rs.getString("content")))
                .setCommentCount(rs.getLong("comment_count"));
    }
}
