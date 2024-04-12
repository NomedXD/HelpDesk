package com.innowise.dto.response;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.UserRole;

import java.util.List;
import java.util.Map;

public record UserInfoResponse(
        String email,
        UserRole role,
        Map<TicketState, List<TicketState>> actions) {
}
