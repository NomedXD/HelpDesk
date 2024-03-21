package com.innowise.util;

import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;

import java.time.LocalDateTime;

public class HistoryBuilder {
    public static History buildHistory(User user, Ticket ticket, HistoryCreationOption historyCreationOption) {
        String historyDescription = "Not defined";
        String historyAction = "Not defined";
        switch (historyCreationOption) {
            case CREATE_TICKET: {
                historyDescription = "Ticket is created";
                historyAction = "Ticket is created";
                break;
            }
            case EDIT_TICKET: {
                historyDescription = "Ticket is edited";
                historyAction = "Ticket is edited";
                break;
            }
        }
        return History.builder()
                .description(historyDescription)
                .action(historyAction)
                .date(LocalDateTime.now())
                .user(user)
                .ticket(ticket)
                .build();
    }

    public static History buildHistory(User user, Ticket ticket, TicketState from, TicketState to) {
        String historyDescription = "Ticket Status is changed";
        String historyAction = String.format("Ticket Status is changed from [%s] to [%s]", from, to);
        return History.builder()
                .description(historyDescription)
                .action(historyAction)
                .date(LocalDateTime.now())
                .user(user)
                .ticket(ticket)
                .build();
    }

    public static History buildHistory(User user, Ticket ticket, HistoryCreationOption historyCreationOption, String fileName) {
        String historyDescription = "Not defined";
        String historyAction = "Not defined";
        switch (historyCreationOption) {
            case TICKET_FILE_ATTACHED: {
                historyDescription = "File is attached";
                historyAction = String.format("File is attached: [%s]", fileName);
                break;
            }
            case TICKET_FILE_REMOVED: {
                historyDescription = "File is removed";
                historyAction = String.format("File is removed: [%s]", fileName);
                break;
            }
        }
        return History.builder()
                .description(historyDescription)
                .action(historyAction)
                .date(LocalDateTime.now())
                .user(user)
                .ticket(ticket)
                .build();
    }
}
