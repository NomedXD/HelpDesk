package com.innowise.util.mapper;

import com.innowise.controller.dto.request.FeedbackRequestDto;
import com.innowise.controller.dto.response.FeedbackResponseDto;
import com.innowise.domain.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "ticketId", target = "ticket.id")
    Feedback toFeedback(FeedbackRequestDto feedbackRequestDto);

    FeedbackResponseDto toFeedbackResponseDto(Feedback feedback);
}
