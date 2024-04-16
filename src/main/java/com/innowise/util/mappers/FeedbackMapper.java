package com.innowise.util.mappers;

import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.domain.Feedback;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FeedbackMapper {
    @Mapping(source = "ticketId", target = "ticket.id")
    Feedback toFeedback(FeedbackRequest feedbackRequest);

    FeedbackResponse toFeedbackResponseDto(Feedback feedback);
}
