package com.pmlido.codechallenge.controller;

import com.pmlido.codechallenge.model.Message;
import com.pmlido.codechallenge.model.MessageInfo;
import com.pmlido.codechallenge.service.MessageRepositoryService;
import com.pmlido.codechallenge.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

	private final String USERID_HEADER = "X-UserId";

	private final MessageRepositoryService messageRepositoryService;
	private final Utils utils;

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> createMessage(@RequestHeader(value=USERID_HEADER) String userId,
									@RequestBody Message message) {

		String msgId = UUID.randomUUID().toString();
		log.trace("New message: userId={}, msgId={}", userId, msgId);

		return messageRepositoryService.insertMessage(new MessageInfo()
				.setMsgId(msgId)
				.setMessage(message.getMessage())
				.setUserId(userId)
				.setDate(utils.getCurrentDate())
				.setTimestamp(Instant.now().getEpochSecond()));
	}

	@PutMapping("/{msgId}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> updateMessage(@RequestHeader(value=USERID_HEADER) String userId,
  								    @PathVariable String msgId,
									@RequestBody Message message) {
		log.trace("Update message: userId={}, msgId={}", userId, msgId);
		return messageRepositoryService.updateMessage(userId, msgId, message.getMessage());
	}

	@DeleteMapping("/{msgId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteMessage(@RequestHeader(value=USERID_HEADER) String userId,
									@PathVariable String msgId) {
		log.trace("Delete message: userId={}, msgId={}", userId, msgId);
		return messageRepositoryService.deleteMessage(userId, msgId);
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public Flux<MessageInfo> getMessages(@RequestHeader(value=USERID_HEADER) String userId) {
		log.trace("Get all messages: userId={}", userId);
		return messageRepositoryService.getAllMessages();
	}
}


