package com.innowise.util.mappers;

import com.innowise.dto.responseDto.AttachmentResponseDto;
import com.innowise.domain.Attachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AttachmentMapper.class)
public interface AttachmentListMapper {
    List<AttachmentResponseDto> toAttachmentResponseDtoList(List<Attachment> attachmentList);
}
