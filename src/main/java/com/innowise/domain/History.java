package com.innowise.domain;

import com.innowise.domain.enums.TicketState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "histories")
public class History {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "action")
    private String action;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    private static final String TICKET_CREATION = "Ticket is created";
    private static final String TICKET_EDITION = "Ticket is edited";
    private static final String STATUS_CHANGED_DESCRIPTION = "Ticket Status is changed";
    private static final String STATUS_CHANGED_ACTION_FORMAT = "Ticket Status is changed from [%s] to [%s]";
    private static final String ATTACH_FILE_DESCRIPTION = "File is attached";
    private static final String REMOVE_FILE_DESCRIPTION = "File is removed";
    private static final String ATTACH_FILE_ACTION_FORMAT = "File is attached: [%s]";
    private static final String REMOVE_FILE_ACTION_FORMAT = "File is attached: [%s]";

    public static History ofCreate(User createdBy, Ticket ticket) {
        return History.builder()
                .description(TICKET_CREATION)
                .action(TICKET_CREATION)
                .date(LocalDateTime.now())
                .user(createdBy)
                .ticket(ticket)
                .build();
    }

    public static History ofUpdate(User createdBy, Ticket ticket) {
        return History.builder()
                .description(TICKET_EDITION)
                .action(TICKET_EDITION)
                .date(LocalDateTime.now())
                .user(createdBy)
                .ticket(ticket)
                .build();
    }

    public static History ofStatusChange( TicketState from, TicketState to, User createdBy, Ticket ticket) {
        return History.builder()
                .description(STATUS_CHANGED_DESCRIPTION)
                .action(STATUS_CHANGED_ACTION_FORMAT.formatted(from, to))
                .date(LocalDateTime.now())
                .user(createdBy)
                .ticket(ticket)
                .build();
    }

    public static History ofFileAttached(String fileName, User createdBy, Ticket ticket) {
        return History.builder()
                .description(ATTACH_FILE_DESCRIPTION)
                .action(ATTACH_FILE_ACTION_FORMAT.formatted(fileName))
                .date(LocalDateTime.now())
                .user(createdBy)
                .ticket(ticket)
                .build();
    }

    public static History ofFileRemoved(String fileName, User createdBy, Ticket ticket) {
        return History.builder()
                .description(REMOVE_FILE_DESCRIPTION)
                .action(REMOVE_FILE_ACTION_FORMAT.formatted(fileName))
                .date(LocalDateTime.now())
                .user(createdBy)
                .ticket(ticket)
                .build();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        History history = (History) o;
        return getId() != null && Objects.equals(getId(), history.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
