package com.example.proxy_server.service;

import com.example.proxy_server.model.UIBean;
import com.example.proxy_server.util.OriginHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Service
public class ProxyService {

    private final Logger logger = LoggerFactory.getLogger(ProxyService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OriginHolder originHolder;


    public UIBean<Object> forwardRequest(
            HttpMethod method,
            HttpServletRequest request,
            byte[] body
    ) {

        String cacheKey = buildCacheKey(method, request);

        if (method == HttpMethod.GET) {
            UIBean<Object> cached = redisService.get(cacheKey, UIBean.class);
            if (cached != null) {
                cached.setSuccess(true);
                cached.setMessage("Cache Hit");
                return cached;
            }
        }

        try {
            ResponseEntity<Object> upstreamResponse;

            if(method == HttpMethod.GET){

                logger.info("Sending upstream request with method : {}", method);

                upstreamResponse = restClient
                        .method(method)
                        .uri(buildUpstreamUri(request))
                        .headers(headers -> copyHeaders(request, headers))
                        .retrieve()
                        .toEntity(Object.class);
            }else {

                logger.info("Sending upstream request with method : {}", method);

                upstreamResponse = restClient
                        .method(method)
                        .uri(buildUpstreamUri(request))
                        .headers(headers -> copyHeaders(request, headers))
                        .body(body)
                        .retrieve()
                        .toEntity(Object.class);
            }

            MediaType contentType = upstreamResponse.getHeaders().getContentType();

            if (contentType == null || !MediaType.APPLICATION_JSON.includes(contentType)) {
                UIBean<Object> error = new UIBean<>();
                error.setSuccess(false);
                error.setMessage("Invalid content type from upstream");
                error.setResponse("Expected application/json but got " + contentType);
                return error;
            }

            Object data = upstreamResponse.getBody();

            logger.info("Response from upstream : {}", data);

            UIBean<Object> response = new UIBean<>();
            response.setData(data);
            response.setSuccess(true);
            response.setMessage("Cache Miss");
            response.setResponse("Upstream call successful");

            if (method == HttpMethod.GET && upstreamResponse.getStatusCode().is2xxSuccessful()) {
                redisService.save(cacheKey, response);
            }else{
                redisService.save(cacheKey, response);
            }

            return response;

        } catch (Exception ex) {

            UIBean<Object> error = new UIBean<>();
            error.setSuccess(false);
            error.setMessage("Upstream call failed");
            error.setResponse(ex.getMessage());

            return error;
        }
    }

    public Void clearCache(){
        logger.info("Clearing all cache...");
        redisService.clearAll();
        logger.info("Cache cleared");
        return null;
    }

    private String buildCacheKey(HttpMethod method, HttpServletRequest request) {
        String cacheKey =  method.name()
                + ":" + request.getRequestURI()
                + "?" + request.getQueryString();

        logger.info("Saving data with cache key : {}", cacheKey);

        return cacheKey;
    }

    private URI buildUpstreamUri(HttpServletRequest request) {

        String path = request.getRequestURI();

        String pathWithoutPrefix = path.replaceFirst("/proxy", "");

        String query = request.getQueryString();

        logger.info("Query param are : {}", query);

        String finalUrl = originHolder.getOrigin()
                + pathWithoutPrefix
                + (query != null ? "?" + query : "");

        logger.info("Final Url is : {}", finalUrl);

        return URI.create(finalUrl);
    }


    private void copyHeaders(HttpServletRequest request, HttpHeaders headers) {
        Collections.list(request.getHeaderNames())
                .forEach(name -> {
                    if (!name.equalsIgnoreCase(HttpHeaders.HOST)
                            && !name.equalsIgnoreCase(HttpHeaders.ACCEPT_ENCODING)
                            && !name.equalsIgnoreCase(HttpHeaders.CONTENT_ENCODING)) {
                        headers.addAll(name, Collections.list(request.getHeaders(name)));
                    }
                });
    }

}
