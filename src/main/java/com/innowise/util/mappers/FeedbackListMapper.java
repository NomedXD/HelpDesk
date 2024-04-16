package com.innowise.util.mappers;

import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.domain.Feedback;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = FeedbackMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FeedbackListMapper {
    List<Feedback> toFeedbackList(List<FeedbackRequest> feedbackRequestList);

    List<FeedbackResponse> toFeedbackResponseDtoList(List<Feedback> feedbackList);
}
