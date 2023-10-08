package com.github.lltal.sarafanserver.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private Object payload;
}
