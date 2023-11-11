package com.aruna.controller;

import com.aruna.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka/api")
public class EventController {

    @Autowired
    PublisherService publisherService;

    @GetMapping("/publish/{message}")
    public CompletableFuture<ResponseEntity<String>> publishMessage(@PathVariable String message) {
        return publisherService.sendMessageToTopic(message)
                .thenApply(offsetId -> ResponseEntity.ok("Message published successfully. Offset: " + offsetId))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to publish message. Error: " + ex.getMessage()));
    }

    @GetMapping("/notification/{message}")
    public ResponseEntity<?> publishNotification(@PathVariable String message) {
        try {
            for (int i = 0; i <= 5000; i++) {
                publisherService.sendMessageToTopic(message + " : " + i);
            }
            return ResponseEntity.ok("message published successfully ..");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


}
