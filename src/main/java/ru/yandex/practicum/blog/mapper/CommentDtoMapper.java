package ru.yandex.practicum.blog.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.blog.dto.CommentDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentDtoMapper implements RowMapper<CommentDto> {

    @Override
    public CommentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentDto()
                .setCommentId(rs.getLong("id"))
                .setContent(rs.getString("content"));
    }
}
