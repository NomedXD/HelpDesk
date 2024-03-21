package com.innowise.util.mapper;

import com.innowise.controller.dto.request.CommentRequestDto;
import com.innowise.controller.dto.response.CommentResponseDto;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "userId", target = "user.id")
    Comment toComment(CommentRequestDto commentRequestDto);

    @Mapping(source = "user.firstName", target = "userName")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
