package com.innowise.util.mapper;

import com.innowise.controller.dto.response.AttachmentResponseDto;
import com.innowise.domain.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentResponseDto toCommentResponseDto(Attachment attachment);
}
