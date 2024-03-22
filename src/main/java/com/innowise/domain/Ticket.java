package com.innowise.domain;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDate desiredResolutionDate;

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

    @OneToMany(mappedBy = "ticketId")
    @ToString.Exclude
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "ticketId")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    private List<History> histories;

    @OneToOne(mappedBy = "ticket")
    private Feedback feedback;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Ticket ticket = (Ticket) o;
        return getId() != null && Objects.equals(getId(), ticket.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
