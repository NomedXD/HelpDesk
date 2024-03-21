package com.innowise.util.mappers;

import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CommentListMapper {
    List<Comment> toCommentList(List<CommentRequest> commentRequestList);

    List<CommentResponse> toCommentResponseDtoList(List<Comment> commentList);
}
