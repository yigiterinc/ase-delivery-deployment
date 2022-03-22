package com.group5.apigateway.config;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.regex.Pattern;

public class Request {
    private HttpMethod httpMethod;
    private Pattern pattern;

    public Request(HttpMethod httpMethod, Pattern pattern) {
        this.httpMethod = httpMethod;
        this.pattern = pattern;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Pattern getPattern() {
        return pattern;
    }
}

