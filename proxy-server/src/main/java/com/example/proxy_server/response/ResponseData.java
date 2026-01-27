package com.example.proxy_server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseData {
    @JsonProperty("bulkResponse")
    private Map<String, String> bulkResponse;
}