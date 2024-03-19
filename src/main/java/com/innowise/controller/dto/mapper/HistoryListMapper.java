package com.innowise.controller.dto.mapper;

import com.innowise.controller.dto.responseDto.HistoryResponseDto;
import com.innowise.domain.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = HistoryMapper.class)
public interface HistoryListMapper {
    List<HistoryResponseDto> toHistoryResponseDtoList(List<History> historyList);
}
