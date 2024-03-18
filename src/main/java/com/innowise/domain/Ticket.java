package com.innowise.domain;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDateTime desiredResolutionDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state_id")
    private TicketState state;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "urgency_id")
    private TicketUrgency urgency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approver_id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "ticket_id")
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "ticket_id")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket")
    private List<History> histories;

    @OneToOne(mappedBy = "ticket")
    private Feedback feedback;
}
