package com.pmlido.codechallenge.service;

import com.pmlido.codechallenge.model.MessageInfo;
import com.pmlido.codechallenge.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageRepositoryService {

    private final Utils utils;

    private final Map<String, Map<String, MessageInfo>> messages = new ConcurrentHashMap<>();

    public Map<String, Map<String, MessageInfo>> getMessageRepo() {
        return messages;
    }

    public Mono<Void> insertMessage(MessageInfo messageInfo) {
        if (!messages.containsKey(messageInfo.getUserId())) {
            messages.put(messageInfo.getUserId(), new HashMap<>());
        }
        messages.get(messageInfo.getUserId()).put(messageInfo.getMsgId(), messageInfo);
        return Mono.empty();
    }

    public Mono<Void> updateMessage(String userId, String msgId, String message) {
        if (!messages.containsKey(userId)) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        if (messages.get(userId).containsKey(msgId)) {
            messages.get(userId).get(msgId).setMessage(message).setDate(utils.getCurrentDate());
        } else {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        return Mono.empty();
    }

    public Mono<Void> deleteMessage(String userId, String msgId) {
        if (messages.containsKey(userId)) {
            messages.get(userId).remove(msgId);
        }
        return Mono.empty();
    }

    public Flux<MessageInfo> getAllMessages() {
        return Flux.fromIterable(messages
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }
}
