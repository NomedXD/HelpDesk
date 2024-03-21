package com.innowise.dto.mappers;

import com.innowise.dto.responseDto.HistoryResponseDto;
import com.innowise.domain.History;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    @Mapping(source = "user.firstName", target = "userName")
    HistoryResponseDto toHistoryResponseDto(History history);
}
