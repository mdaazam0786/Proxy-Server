package com.example.proxy_server.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagMasterApiResponse {
    private String uri;
    private HttpStatusCode httpStatusCode;
    private boolean success;
    private ResponseData data;
}

