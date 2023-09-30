package com.github.lltal.sarafanserver.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    private final ObjectType objectType;
    private final EventType eventType;
    @JsonRawValue
    private final String payload;
}
