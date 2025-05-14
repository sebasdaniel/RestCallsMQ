package com.demo.sporty.service.producer;

public interface ProducerService {

    void send(String key, String message);
}
