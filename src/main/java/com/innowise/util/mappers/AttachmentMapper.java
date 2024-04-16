package com.innowise.util.mappers;

import com.innowise.dto.response.AttachmentResponse;
import com.innowise.domain.Attachment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

// TODO Attachment mapper does not uses, think to remove from project
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AttachmentMapper {
    AttachmentResponse toCommentResponseDto(Attachment attachment);
}
