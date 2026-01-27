package com.example.proxy_server.controller;

import com.example.proxy_server.model.UIBean;
import com.example.proxy_server.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH
        }
    )
    public ResponseEntity<UIBean<Object>> proxyRequest(
            HttpMethod method,
            HttpServletRequest request,
            @RequestBody(required = false) byte[] body
    ) {
        UIBean<Object> response;

        response = proxyService.forwardRequest(method, request, body);

        if (request != null){
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.internalServerError().body(response);

    }

    @RequestMapping(value = "/clear")
    public ResponseEntity<UIBean<?>> clearCache(){
        UIBean<Void> response = new UIBean<>();

        response.setData(proxyService.clearCache());
        response.setSuccess(true);
        response.setMessage("Cache cleared successfully");
        response.setResponse("Cleared");

        return ResponseEntity.ok(response);
    }
}