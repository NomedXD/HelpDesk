package com.innowise.util.mappers;

import com.innowise.dto.responseDto.AttachmentResponseDto;
import com.innowise.domain.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentResponseDto toCommentResponseDto(Attachment attachment);
}
