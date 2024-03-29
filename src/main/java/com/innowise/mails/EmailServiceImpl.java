package com.innowise.mails;

import com.innowise.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableAsync
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    @Value("${mail.sender}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    @Async
    public void notifyFromDraftDeclinedToNew(List<User> managerList, Integer ticketId) {
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

    @Override
    @Async
    public void notifyFromNewToApproved(List<User> creatorEngineersList, Integer ticketId) {
        for (User user : creatorEngineersList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(user.getEmail());
            message.setSubject("Ticket was approved");
            message.setText(String.format("""
                    Dear Users,
                    Ticket %d was approved by the Manager.
                    """, ticketId));
            emailSender.send(message);
        }
    }

    @Override
    @Async
    public void notifyFromNewToDeclined(User creator, Integer ticketId) {
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

    @Override
    @Async
    public void notifyFromNewToCancelled(User creator, Integer ticketId) {
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

    @Override
    @Async
    public void notifyFromApprovedToCancelled(List<User> creatorApproverList, Integer ticketId) {
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

    @Override
    @Async
    public void notifyInProgressToDone(User creator, Integer ticketId) {
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
}
