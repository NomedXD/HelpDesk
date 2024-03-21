package com.innowise.util.mapper;

import com.innowise.controller.dto.request.CommentRequestDto;
import com.innowise.controller.dto.response.CommentResponseDto;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CommentListMapper {
    List<Comment> toCommentList(List<CommentRequestDto> commentRequestDtoList);

    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> commentList);
}
