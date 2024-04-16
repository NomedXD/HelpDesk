package com.innowise.util.mappers;

import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {
    @Mapping(source = "ticketId", target = "ticket.id")
    Comment toComment(CommentRequest commentRequest);

    @Mapping(source = "user.email", target = "userEmail")
    CommentResponse toCommentResponseDto(Comment comment);
}
