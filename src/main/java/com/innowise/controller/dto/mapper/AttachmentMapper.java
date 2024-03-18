package com.innowise.controller.dto.mapper;

import com.innowise.controller.dto.responseDto.AttachmentResponseDto;
import com.innowise.domain.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentResponseDto toCommentResponseDto(Attachment attachment);
}
