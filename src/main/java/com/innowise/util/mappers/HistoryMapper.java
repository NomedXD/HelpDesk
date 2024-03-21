package com.innowise.util.mappers;

import com.innowise.dto.response.HistoryResponse;
import com.innowise.domain.History;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    @Mapping(source = "user.firstName", target = "userName")
    HistoryResponse toHistoryResponseDto(History history);
}
