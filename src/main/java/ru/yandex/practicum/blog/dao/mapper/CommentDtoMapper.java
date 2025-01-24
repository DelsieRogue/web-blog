package ru.yandex.practicum.blog.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.blog.dto.CommentViewDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentDtoMapper implements RowMapper<CommentViewDto> {

    @Override
    public CommentViewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentViewDto()
                .setId(rs.getLong("id"))
                .setContent(rs.getString("content"));
    }
}
