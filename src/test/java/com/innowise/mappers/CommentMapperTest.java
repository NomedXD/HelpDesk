package com.innowise.mappers;

import com.innowise.domain.Comment;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.util.mappers.CommentMapper;
import com.innowise.util.mappers.CommentMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {
    private final CommentMapper commentMapper;

    public CommentMapperTest() {
        this.commentMapper = new CommentMapperImpl();
    }

    @Test
    public void toComment_withFullFields_mappingSameFieldsToComment() {
        // CommentRequest
        String commentRequestText = "commentRequestText";
        Integer commentRequestTicketId = 1;
        CommentRequest commentRequest = CommentRequest.builder().text(commentRequestText).ticketId(commentRequestTicketId).build();

        // Comment
        Comment expectedComment = Comment.builder().text(commentRequestText).
                ticket(Ticket.builder().id(commentRequestTicketId).build()).build();

        Comment actualComment = commentMapper.toComment(commentRequest);
        Assertions.assertEquals(expectedComment.getText(), actualComment.getText());
        Assertions.assertEquals(expectedComment.getTicket().getId(), actualComment.getTicket().getId());
    }

    @Test
    public void toFeedbackResponseDto_withFullFields_mappingSameFieldsToFeedbackResponse() {
        // Comment
        String commentText = "commentText";
        String commentUserName = "userName";
        LocalDateTime commentDate = LocalDateTime.now();

        Comment comment = Comment.builder().text(commentText).user(User.builder().firstName(commentUserName).build()).
                date(commentDate).build();

        // CommentResponse
        CommentResponse expectedCommentResponse = CommentResponse.builder().date(commentDate).
                userName(commentUserName).text(commentText).build();

        Assertions.assertEquals(expectedCommentResponse, commentMapper.toCommentResponseDto(comment));

    }
}
