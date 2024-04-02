package com.innowise.mails;

import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;

public interface EmailService {

    void notifyFeedbackProvide(User assignee, Integer ticketId);

    void notifyTicketStateTransfer(Ticket ticket, TicketState toTicketState);
}
