package com.crewmeister.cmcodingchallenge.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final RestTemplate template;

    public ResourceServiceImpl(RestTemplate template) {
        this.template = template;
    }

    @Override
    public <T> T getMethod(String url, Class<T> t) {
        return template.getForObject(url,t);
    }
}
