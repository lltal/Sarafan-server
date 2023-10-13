package com.github.lltal.sarafanserver.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsMessageDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String payload;
}