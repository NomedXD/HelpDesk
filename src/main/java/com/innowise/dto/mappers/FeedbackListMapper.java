package com.innowise.dto.mappers;

import com.innowise.dto.requestDto.FeedbackRequestDto;
import com.innowise.dto.responseDto.FeedbackResponseDto;
import com.innowise.domain.Feedback;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = FeedbackMapper.class)
public interface FeedbackListMapper {
    List<Feedback> toFeedbackList(List<FeedbackRequestDto> feedbackRequestDtoList);

    List<FeedbackResponseDto> toFeedbackResponseDtoList(List<Feedback> feedbackList);
}
