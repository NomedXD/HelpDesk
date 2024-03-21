package com.innowise.util.mappers;

import com.innowise.dto.responseDto.HistoryResponseDto;
import com.innowise.domain.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = HistoryMapper.class)
public interface HistoryListMapper {
    List<HistoryResponseDto> toHistoryResponseDtoList(List<History> historyList);
}
