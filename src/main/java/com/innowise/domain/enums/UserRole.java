package com.innowise.domain.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

@Getter
public enum UserRole implements GrantedAuthority {
    ROLE_MANAGER(Map.ofEntries(
            Map.entry(TicketState.DRAFT, List.of(TicketState.NEW, TicketState.CANCELED)),
            Map.entry(TicketState.NEW, List.of(TicketState.APPROVED, TicketState.DECLINED, TicketState.CANCELED)),
            Map.entry(TicketState.DECLINED, List.of(TicketState.NEW, TicketState.CANCELED)))),
    ROLE_EMPLOYEE(Map.ofEntries(
            Map.entry(TicketState.DRAFT, List.of(TicketState.NEW, TicketState.CANCELED)),
            Map.entry(TicketState.DECLINED, List.of(TicketState.NEW, TicketState.CANCELED)))),
    ROLE_ENGINEER(Map.ofEntries(
            Map.entry(TicketState.APPROVED, List.of(TicketState.IN_PROGRESS, TicketState.CANCELED)),
            Map.entry(TicketState.IN_PROGRESS, List.of(TicketState.DONE))));

    private final Map<TicketState, List<TicketState>> fromToStateAuthorities;

    UserRole(Map<TicketState, List<TicketState>> fromToStateAuthorities) {
        this.fromToStateAuthorities = fromToStateAuthorities;
    }

    public String getAuthority() {
        return this.name();
    }
}
