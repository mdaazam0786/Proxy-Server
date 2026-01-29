package com.example.proxy_server.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
public class StoreFrontClient {
    @Autowired
    private RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> goldExamListing(Map<String, Object> request){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString("http://localhost:8081/proxy/gold/v1/exam/course/list");

        request.forEach((key, value) -> uriBuilder.queryParam(key, value));

        String url = uriBuilder.toUriString();

        // Make GET request
        ResponseEntity<String> response = restClient.get()
                .uri(url)
                .headers(headers -> {
                    headers.set("accept", "*/*");
                    headers.set("accept-language", "en-US,en;q=0.9");
                    headers.set("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9..."); // your token
                    headers.set("content-type", "application/json");
                    headers.set("origin", "https://newadminui-sigmaqa.com");
                    headers.set("priority", "u=1, i");
                    headers.set("referer", "https://newadminui-sigmaqa.com/");
                    headers.set("sec-ch-ua", "\"Chromium\";v=\"142\", \"Google Chrome\";v=\"142\", \"Not_A Brand\";v=\"99\"");
                    headers.set("sec-ch-ua-mobile", "?0");
                    headers.set("sec-ch-ua-platform", "\"Linux\"");
                    headers.set("sec-fetch-dest", "empty");
                    headers.set("sec-fetch-mode", "cors");
                    headers.set("sec-fetch-site", "same-site");
                    headers.set("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36");
                })
                .retrieve()
                .toEntity(String.class);

        try {
            // Convert JSON string to Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }


}
