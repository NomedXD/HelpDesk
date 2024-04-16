package com.innowise.mappers;

import com.innowise.domain.Attachment;
import com.innowise.domain.Category;
import com.innowise.domain.Comment;
import com.innowise.domain.Feedback;
import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.response.TicketResponse;
import com.innowise.util.mappers.AttachmentListMapperImpl;
import com.innowise.util.mappers.AttachmentMapperImpl;
import com.innowise.util.mappers.CommentListMapper;
import com.innowise.util.mappers.CommentListMapperImpl;
import com.innowise.util.mappers.CommentMapperImpl;
import com.innowise.util.mappers.HistoryListMapperImpl;
import com.innowise.util.mappers.HistoryMapperImpl;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.util.mappers.TicketMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TicketMapperTest {
    private final TicketMapper ticketMapper;
    private final CommentListMapper commentListMapper;

    public TicketMapperTest() {
        this.ticketMapper = new TicketMapperImpl(
                new HistoryListMapperImpl(new HistoryMapperImpl()),
                new CommentListMapperImpl(new CommentMapperImpl()),
                new AttachmentListMapperImpl(new AttachmentMapperImpl()));
        this.commentListMapper = new CommentListMapperImpl(new CommentMapperImpl());
    }

    @Test
    public void toTicketResponseDto_withFullFields_mappingSameFieldsToTicketResponse() {
        // User
        Integer userId = 1;
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        UserRole userRole = UserRole.ROLE_MANAGER;
        String password = "password";
        List<Ticket> ticketsAsAssignee = List.of();
        List<Ticket> ticketsAsOwner = new ArrayList<>();
        List<Ticket> ticketsAsApprover = List.of();
        List<History> history = List.of();
        List<Feedback> feedback = List.of();
        List<Comment> comments = new ArrayList<>();
        User user = User.builder().id(userId).firstName(firstName).lastName(lastName).email(email).role(userRole).
                password(password).ticketsAsAssignee(ticketsAsAssignee).ticketsAsOwner(ticketsAsOwner).
                ticketsAsApprover(ticketsAsApprover).history(history).feedback(feedback).comments(comments).build();

        //Comment
        Integer commentId = 1;
        String commentText = "commentText";
        LocalDateTime commentDate = LocalDateTime.now();
        Comment comment = Comment.builder().id(commentId).text(commentText).date(commentDate).user(user).build();
        user.getComments().add(comment);

        // Ticket
        Integer ticketId = 1;
        String ticketName = "ticketName";
        String ticketDescription = "ticketDescription";
        LocalDate ticketCreatedOn = LocalDate.now();
        LocalDate ticketDesiredResolutionDate = ticketCreatedOn.plusDays(1);
        TicketState ticketState = TicketState.NEW;
        TicketUrgency ticketUrgency = TicketUrgency.HIGH;
        Category ticketCategory = Category.builder().id(1).name("category").build();
        List<Attachment> attachments = List.of();
        List<History> ticketHistories = List.of();
        Ticket ticket = Ticket.builder().id(ticketId).name(ticketName).description(ticketDescription).createdOn(ticketCreatedOn).
                desiredResolutionDate(ticketDesiredResolutionDate).state(ticketState).urgency(ticketUrgency).
                assignee(null).owner(user).approver(null).category(ticketCategory).attachments(attachments).
                comments(List.of(comment)).histories(ticketHistories).feedback(null).build();
        user.getTicketsAsOwner().add(ticket);

        // TicketResponse
        TicketResponse expectedTicketResponse = TicketResponse.builder().id(ticketId).name(ticketName).
                createdOn(ticketCreatedOn).state(ticketState).urgency(ticketUrgency).
                categoryName(ticketCategory.getName()).desiredResolutionDate(ticketDesiredResolutionDate).
                ownerName(user.getFirstName()).approverName(ticket.getApprover() == null ? null : ticket.getApprover().
                        getFirstName()).assigneeName(ticket.getAssignee() == null ? null : ticket.getAssignee().
                        getFirstName()).attachments(List.of()).description(ticketDescription).
                historyResponseList(List.of()).commentResponseList(commentListMapper.toCommentResponseDtoList(ticket.getComments())).build();

        Assertions.assertEquals(expectedTicketResponse, ticketMapper.toTicketResponseDto(ticket));
    }
}
