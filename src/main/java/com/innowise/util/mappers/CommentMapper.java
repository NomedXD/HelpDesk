package com.innowise.util.mappers;

import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest commentRequest);

    @Mapping(source = "user.firstName", target = "userName")
    CommentResponse toCommentResponseDto(Comment comment);
}
