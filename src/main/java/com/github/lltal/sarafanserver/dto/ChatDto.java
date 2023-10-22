package com.github.lltal.sarafanserver.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

@Data
public class ChatDto {
    private String id;
    @JsonRawValue
    private String messages;
    @JsonRawValue
    private String users;
}
