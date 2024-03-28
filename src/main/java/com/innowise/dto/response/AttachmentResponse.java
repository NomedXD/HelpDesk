package com.innowise.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentResponse{
    private Integer id;
    private String url;
    private String name;
    private String type;
    private Long size;
}
