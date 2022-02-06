package com.crewmeister.cmcodingchallenge.service;


public interface ResourceService {

     <T> T getMethod(String url, Class<T> t);

}
