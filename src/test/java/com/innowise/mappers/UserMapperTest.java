package com.innowise.mappers;

import com.innowise.domain.Comment;
import com.innowise.domain.Feedback;
import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.response.UserResponse;
import com.innowise.util.mappers.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void toUserResponseDto_withFullFields_mappingSameFieldsToUserResponse() {
        // User
        Integer userId = 1;
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        UserRole userRole = UserRole.ROLE_MANAGER;
        String password = "password";
        List<Ticket> ticketsAsAssignee = List.of();
        List<Ticket> ticketsAsOwner = List.of();
        List<Ticket> ticketsAsApprover = List.of();
        List<History> history = List.of();
        List<Feedback> feedback = List.of();
        List<Comment> comments = new ArrayList<>();
        User user = User.builder().id(userId).firstName(firstName).lastName(lastName).email(email).role(userRole).
                password(password).ticketsAsAssignee(ticketsAsAssignee).ticketsAsOwner(ticketsAsOwner).
                ticketsAsApprover(ticketsAsApprover).history(history).feedback(feedback).comments(comments).build();

        //Comment
        Integer commentId = 1;
        String commentText = "commentText";
        LocalDateTime commentDate = LocalDateTime.now();
        Comment comment = Comment.builder().id(commentId).text(commentText).date(commentDate).user(user).build();
        user.getComments().add(comment);

        // UserResponse
        UserResponse expectedUserResponse = UserResponse.builder().id(userId).firstName(firstName).lastName(lastName).email(email).build();

        Assertions.assertEquals(expectedUserResponse, userMapper.toUserResponse(user));

    }
}
