package com.example.proxy_server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UIBean <T>{
    @JsonProperty("data")
    T data;
    boolean success;
    String message;
    String response;
}
