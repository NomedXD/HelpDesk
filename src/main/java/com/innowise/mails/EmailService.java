package com.innowise.mails;

import com.innowise.domain.User;

import java.util.List;

public interface EmailService {
    void notifyFromDraftDeclinedToNew(List<User> managerList, Integer ticketId);

    void notifyFromNewToApproved(List<User> creatorEngineersList, Integer ticketId);

    void notifyFromNewToDeclined(User creator, Integer ticketId);

    void notifyFromNewToCancelled(User creator, Integer ticketId);

    void notifyFromApprovedToCancelled(List<User> creatorApproverList, Integer ticketId);

    void notifyInProgressToDone(User creator, Integer ticketId);

    void notifyFeedbackProvide(User assignee, Integer ticketId);
}
