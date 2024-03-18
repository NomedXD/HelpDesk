package com.innowise.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate")
    private byte rate;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "text")
    private String text;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
