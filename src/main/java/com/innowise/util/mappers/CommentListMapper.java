package com.innowise.util.mappers;

import com.innowise.dto.requestDto.CommentRequestDto;
import com.innowise.dto.responseDto.CommentResponseDto;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CommentListMapper {
    List<Comment> toCommentList(List<CommentRequestDto> commentRequestDtoList);

    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> commentList);
}
