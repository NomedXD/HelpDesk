package com.innowise.util.mappers;

import com.innowise.dto.response.AttachmentResponse;
import com.innowise.domain.Attachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AttachmentMapper.class)
public interface AttachmentListMapper {
    List<AttachmentResponse> toAttachmentResponseDtoList(List<Attachment> attachmentList);
}
