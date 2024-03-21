package com.innowise.util.mappers;

import com.innowise.dto.response.HistoryResponse;
import com.innowise.domain.History;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = HistoryMapper.class)
public interface HistoryListMapper {
    List<HistoryResponse> toHistoryResponseDtoList(List<History> historyList);
}
