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
import com.innowise.dto.response.HistoryResponse;
import com.innowise.util.mappers.HistoryMapper;
import com.innowise.util.mappers.HistoryMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HistoryMapperTest {
    private final HistoryMapper historyMapper;

    public HistoryMapperTest() {
        this.historyMapper = new HistoryMapperImpl();
    }

    @Test
    public void toHistoryResponseDto_withFullFields_mappingSameFieldsToHistoryResponse() {
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
        List<History> ticketHistory = List.of();
        List<Feedback> feedback = List.of();
        List<Comment> comments = new ArrayList<>();
        User user = User.builder().id(userId).firstName(firstName).lastName(lastName).email(email).role(userRole).
                password(password).ticketsAsAssignee(ticketsAsAssignee).ticketsAsOwner(ticketsAsOwner).
                ticketsAsApprover(ticketsAsApprover).history(ticketHistory).feedback(feedback).comments(comments).build();

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

        // History
        Integer historyId = 1;
        LocalDateTime historyDate = LocalDateTime.now();
        String historyAction = "historyAction";
        String historyDescription = "historyDescription";
        History history = History.builder().id(historyId).ticket(ticket).date(historyDate).
                action(historyAction).user(user).description(historyDescription).build();

        // HistoryResponse
        HistoryResponse expectedHistoryResponse = HistoryResponse.builder().date(historyDate).userName(history.getUser().getFirstName()).
                action(historyAction).description(historyDescription).build();

        Assertions.assertEquals(expectedHistoryResponse, historyMapper.toHistoryResponseDto(history));
    }
}
