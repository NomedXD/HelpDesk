package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketUrgency;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.repositories.TicketRepository;
import com.innowise.services.CategoryService;
import com.innowise.services.UserService;
import com.innowise.util.mappers.AttachmentListMapperImpl;
import com.innowise.util.mappers.AttachmentMapperImpl;
import com.innowise.util.mappers.CommentListMapperImpl;
import com.innowise.util.mappers.CommentMapperImpl;
import com.innowise.util.mappers.HistoryListMapperImpl;
import com.innowise.util.mappers.HistoryMapperImpl;
import com.innowise.util.mappers.TicketListMapper;
import com.innowise.util.mappers.TicketListMapperImpl;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.util.mappers.TicketMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @InjectMocks
    private TicketServiceImpl ticketService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CategoryService categoryService;
    @Spy
    private TicketListMapper ticketListMapper = new TicketListMapperImpl(new TicketMapperImpl(new HistoryListMapperImpl(new HistoryMapperImpl()),
            new CommentListMapperImpl(new CommentMapperImpl()), new AttachmentListMapperImpl(new AttachmentMapperImpl())));
    @Spy
    private TicketMapper ticketMapper = new TicketMapperImpl(new HistoryListMapperImpl(new HistoryMapperImpl()),
            new CommentListMapperImpl(new CommentMapperImpl()), new AttachmentListMapperImpl(new AttachmentMapperImpl()));
    @Mock
    private UserService userService;

    @Test
    public void save_withValidData_savesTicket() {
        // CreateTicketRequest
        Integer createTicketRequestCategoryId = 1;
        String createTicketRequestName = "createTicketRequestName";
        String createTicketRequestDescription = "createTicketRequestDescription";
        TicketUrgency createTicketRequestTicketUrgency = TicketUrgency.LOW;
        LocalDate createTicketRequestDesiredResolutionDate = LocalDate.now();
        byte[] fileBytes = "test".getBytes();
        MultipartFile[] createTicketRequestMultipartFile = new MultipartFile[]{new MockMultipartFile("test", fileBytes)};
        CreateTicketRequest request = CreateTicketRequest.builder().categoryId(createTicketRequestCategoryId).
                name(createTicketRequestName).description(createTicketRequestDescription).
                urgency(createTicketRequestTicketUrgency).desiredResolutionDate(createTicketRequestDesiredResolutionDate).
                files(createTicketRequestMultipartFile).isDraft(null).build();

        String contextUserName = "user@example.com";

        Category category = Category.builder().build();
        User owner = User.builder().build();

        Mockito.when(categoryService.findById(request.categoryId())).thenReturn(category);
        Mockito.when(userService.findByEmailService(contextUserName)).thenReturn(owner);
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(Ticket.builder().attachments(new ArrayList<>()).build());

        TicketResponse result = ticketService.save(request, contextUserName);
        Mockito.verify(categoryService, Mockito.times(1)).findById(request.categoryId());
        Mockito.verify(userService, Mockito.times(1)).findByEmailService(contextUserName);
        Mockito.verify(ticketRepository, Mockito.times(1)).save(Mockito.any(Ticket.class));
        Assertions.assertNotNull(result);
    }

    @Test
    public void findById_withValidTicketId_returnsTicket() {
        Integer ticketId = 1;
        Ticket expectedTicket = Ticket.builder().id(ticketId).attachments(new ArrayList<>()).build();

        Mockito.when(ticketRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.of(expectedTicket));

        // TicketResponse
        TicketResponse actualTicketResponse = ticketService.findById(ticketId);

        Mockito.verify(ticketRepository, Mockito.times(1)).findById(ticketId);
        Assertions.assertNotNull(actualTicketResponse);
        Assertions.assertEquals(expectedTicket.getId(), actualTicketResponse.getId());
    }

    @Test
    public void findById_withInvalidTicketId_throws() {
        Integer invalidTicketId = 999;

        Mockito.when(ticketRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchEntityIdException.class, () -> ticketService.findById(invalidTicketId));
        Mockito.verify(ticketRepository, Mockito.times(1)).findById(invalidTicketId);
    }

    @Test
    public void findAll_withValid_returnsTicketList() {
        String ticket1Name = "ticket1Name";
        String ticket2Name = "ticket2Name";
        List<Ticket> expectedTicketList = List.of(
                Ticket.builder().name(ticket1Name).build(),
                Ticket.builder().name(ticket2Name).build());
        List<TicketResponse> expectedTicketResponseList = List.of(TicketResponse.builder().name(ticket1Name).build(),
                TicketResponse.builder().name(ticket2Name).build());
        Mockito.when(ticketRepository.findAll()).thenReturn(expectedTicketList);
        List<TicketResponse> actualTickeResponseList = ticketService.findAll();

        Assertions.assertEquals(expectedTicketResponseList, actualTickeResponseList);
        Mockito.verify(ticketRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllByAssigneeEmail_withValid_returnsTicketList() {
        String assigneeEmail = "assigneeEmail";
        Integer userId = 1;

        Mockito.when(userService.findByEmail(assigneeEmail)).thenReturn(UserResponse.builder().id(userId).build());
        Mockito.when(ticketRepository.findAllByAssigneeId(userId)).thenReturn(new ArrayList<>());
        List<TicketResponse> actualTickeResponseList = ticketService.findAllByAssigneeEmail(assigneeEmail);

        Assertions.assertNotNull(actualTickeResponseList);
        Mockito.verify(ticketRepository, Mockito.times(1)).findAllByAssigneeId(userId);
        Mockito.verify(userService, Mockito.times(1)).findByEmail(assigneeEmail);
    }
}
