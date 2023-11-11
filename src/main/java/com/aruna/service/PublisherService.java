package com.aruna.service;

import com.aruna.constant.ConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class PublisherService {
    @Autowired
    private KafkaTemplate<String,Object> template;

    public CompletableFuture<String> sendMessageToTopic(String message) {
        AtomicReference<String> offsetId = new AtomicReference<>("0");
        CompletableFuture<SendResult<String, Object>> sendResult = template.send(ConfigConstant.kafkaTopic, message);

        CompletableFuture<String> resultFuture = new CompletableFuture<>();

        sendResult.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");

                offsetId.set(String.valueOf(result.getRecordMetadata().offset()));
                resultFuture.complete(offsetId.get());
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
                resultFuture.completeExceptionally(ex);
            }
        });

        return resultFuture;
    }


}
