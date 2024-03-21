package com.innowise.util.mapper;

import com.innowise.controller.dto.response.HistoryResponseDto;
import com.innowise.domain.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = HistoryMapper.class)
public interface HistoryListMapper {
    List<HistoryResponseDto> toHistoryResponseDtoList(List<History> historyList);
}
