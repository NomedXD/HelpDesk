package com.innowise.controller.dto.mapper;

import com.innowise.controller.dto.requestDto.CommentRequestDto;
import com.innowise.controller.dto.responseDto.CommentResponseDto;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CommentListMapper {
    List<Comment> toCommentList(List<CommentRequestDto> commentRequestDtoList);

    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> commentList);
}
