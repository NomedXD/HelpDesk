package com.innowise.mails;

import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.UserRole;
import com.innowise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    @Value("${mail.sender}")
    private String sender;
    private final UserService userService;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, UserService userService) {
        this.emailSender = emailSender;
        this.userService = userService;
    }

    @Override
    @Async
    public void notifyFeedbackProvide(User assignee, Integer ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(assignee.getEmail());
        message.setSubject("Feedback was provided");
        message.setText(String.format("""
                Dear %s %s,
                The feedback was provided on ticket %d.
                """, assignee.getFirstName(), assignee.getLastName(), ticketId));
        emailSender.send(message);
    }

    @Override
    public void notifyTicketStateTransfer(Ticket ticket, TicketState toTicketState) {
        TicketState currentState = ticket.getState();
        if ((currentState.equals(TicketState.DRAFT) || currentState.equals(TicketState.DECLINED)) && toTicketState.equals(TicketState.NEW)) {
            List<User> managerList = userService.findAll().stream().filter(user -> user.getRole().equals(UserRole.ROLE_MANAGER)).toList();
            notifyFromDraftDeclinedToNew(managerList, ticket.getId());
        }
        if (currentState.equals(TicketState.NEW) && toTicketState.equals(TicketState.APPROVED)) {
            List<User> creatorEngineerList = userService.findAll().stream().filter(user -> user.getRole().equals(UserRole.ROLE_ENGINEER)).collect(Collectors.toList());
            creatorEngineerList.add(ticket.getOwner());
            notifyFromNewToApproved(creatorEngineerList, ticket.getId());
        }
        if (currentState.equals(TicketState.NEW) && toTicketState.equals(TicketState.DECLINED)) {
            notifyFromNewToDeclined(ticket.getOwner(), ticket.getId());
        }
        if (currentState.equals(TicketState.NEW) && toTicketState.equals(TicketState.CANCELED)) {
            notifyFromNewToCancelled(ticket.getOwner(), ticket.getId());
        }
        if (currentState.equals(TicketState.APPROVED) && toTicketState.equals(TicketState.CANCELED)) {
            notifyFromApprovedToCancelled(List.of(ticket.getOwner(), ticket.getApprover()), ticket.getId());
        }
        if (currentState.equals(TicketState.IN_PROGRESS) && toTicketState.equals(TicketState.DONE)) {
            notifyFromInProgressToDone(ticket.getOwner(), ticket.getId());
        }
    }
    @Async
    protected void notifyFromDraftDeclinedToNew(List<User> managerList, Integer ticketId) {
        for (User user : managerList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(user.getEmail());
            message.setSubject("New ticket for approval");
            message.setText(String.format("""
                    Dear Managers,
                    New ticket %d is waiting for your approval.
                    """, ticketId));
            emailSender.send(message);
        }
    }
    @Async
    protected void notifyFromNewToApproved(List<User> creatorEngineerList, Integer ticketId) {
        for (User user : creatorEngineerList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo("coovshow1@gmail.com"); // user.getEmail()
            message.setSubject("Ticket was approved");
            message.setText(String.format("""
                    Dear Users,
                    Ticket %d was approved by the Manager.
                    """, ticketId));
            emailSender.send(message);
        }
    }
    @Async
    protected void notifyFromNewToDeclined(User creator, Integer ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(creator.getEmail());
        message.setSubject("Ticket was declined");
        message.setText(String.format("""
                Dear %s %s,
                Ticket %d was declined by the Manager.
                """, creator.getFirstName(), creator.getLastName(), ticketId));
        emailSender.send(message);
    }
    @Async
    protected void notifyFromNewToCancelled(User creator, Integer ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(creator.getEmail());
        message.setSubject("Ticket was cancelled");
        message.setText(String.format("""
                Dear %s %s,
                Ticket %d was cancelled by the Manager.
                """, creator.getFirstName(), creator.getLastName(), ticketId));
        emailSender.send(message);
    }
    @Async
    protected void notifyFromApprovedToCancelled(List<User> creatorApproverList, Integer ticketId) {
        for (User user : creatorApproverList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(user.getEmail());
            message.setSubject("Ticket was cancelled");
            message.setText(String.format("""
                    Dear Users,
                    Ticket %d was cancelled by the Engineer.
                    """, ticketId));
            emailSender.send(message);
        }
    }
    @Async
    protected void notifyFromInProgressToDone(User creator, Integer ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(creator.getEmail());
        message.setSubject("Ticket was done");
        message.setText(String.format("""
                Dear %s %s,
                Ticket %d was done by the Engineer.
                Please provide your feedback clicking on the ticket.
                """, creator.getFirstName(), creator.getLastName(), ticketId));
        emailSender.send(message);
    }
}
