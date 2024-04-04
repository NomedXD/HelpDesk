package com.innowise.mappers;

import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.dto.request.FeedbackRequest;
import com.innowise.util.mappers.FeedbackMapper;
import com.innowise.util.mappers.FeedbackMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeedbackMapperTest {
    private final FeedbackMapper feedbackMapper;

    public FeedbackMapperTest() {
        this.feedbackMapper = new FeedbackMapperImpl();
    }

    @Test
    public void toFeedbackResponseDto_withFullFields_mappingSameFieldsToFeedbackResponse() {
        // FeedbackRequest
        byte feedbackRequestRate = 4;
        String feedbackRequestText = "feedbackRequestText";
        Integer feedbackRequestTicketId = 1;
        FeedbackRequest feedbackRequest = FeedbackRequest.builder().rate(feedbackRequestRate).
                text(feedbackRequestText).ticketId(feedbackRequestTicketId).build();

        // Feedback
        Feedback expectedFeedback = Feedback.builder().rate(feedbackRequestRate).text(feedbackRequestText).
                ticket(Ticket.builder().id(feedbackRequestTicketId).build()).build();

        Feedback actualFeedback = feedbackMapper.toFeedback(feedbackRequest);
        Assertions.assertEquals(expectedFeedback.getId(), actualFeedback.getId());
        Assertions.assertEquals(expectedFeedback.getRate(), actualFeedback.getRate());
        Assertions.assertEquals(expectedFeedback.getText(), actualFeedback.getText());
        Assertions.assertEquals(expectedFeedback.getTicket().getId(), actualFeedback.getTicket().getId());

    }
}
