package com.pmlido.codechallenge;

import com.pmlido.codechallenge.controller.MessageController;
import com.pmlido.codechallenge.model.Message;
import com.pmlido.codechallenge.model.MessageInfo;
import com.pmlido.codechallenge.service.MessageRepositoryService;
import com.pmlido.codechallenge.util.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(MessageController.class)
@Import({MessageRepositoryService.class, Utils.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CodechallengeApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private MessageRepositoryService messageRepositoryService;

	@Autowired
	private Utils utils;

	@BeforeAll
	public void setup() {
		webTestClient = webTestClient
				.mutate()
				.responseTimeout(Duration.ofMinutes(20))
				.build();
	}

	@BeforeEach
	public void init() {
		messageRepositoryService.getMessageRepo().clear();
	}

	@Test
	void shouldAddMessageToRepository() {
		String userId = UUID.randomUUID().toString();

		//
		// Verify message is inserted when empty
		//
		webTestClient.post()
				.uri("/message")
				.header("X-UserId", userId)
				.bodyValue(new Message().setMessage("Hello there !"))
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(1);
		assertThat(messageRepositoryService.getMessageRepo().get(userId).values().iterator().next().getMessage()).isEqualTo("Hello there !");
		assertThat(messageRepositoryService.getMessageRepo().get(userId).values().iterator().next().getUserId()).isEqualTo(userId);

		//
		// Verify message is added
		//
		webTestClient.post()
				.uri("/message")
				.header("X-UserId", userId)
				.bodyValue(new Message().setMessage("Hello again !"))
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(2);
	}

	@Test
	void shouldUpdateMessageInRepository() {
		String userId = UUID.randomUUID().toString();
		MessageInfo msg = insertMessage(userId, "First Message");

		//
		// Verify message is updated
		//
		webTestClient.put()
				.uri("/message/" + msg.getMsgId())
				.header("X-UserId", userId)
				.bodyValue(new Message().setMessage("Message update"))
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(1);
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getMessage()).isEqualTo("Message update");
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getUserId()).isEqualTo(userId);
	}

	@Test
	void shouldFailWithUpdateMessageIfMsgNotFound() {
		String userId = UUID.randomUUID().toString();
		MessageInfo msg = insertMessage(userId, "First Message");

		//
		// Try to update message
		//
		webTestClient.put()
				.uri("/message/NON-EXISTING-MSGID")
				.header("X-UserId", userId)
				.bodyValue(new Message().setMessage("Message update"))
				.exchange()
				.expectStatus()
				.isNotFound();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(1);
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getMessage()).isEqualTo("First Message");
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getUserId()).isEqualTo(userId);
	}

	@Test
	void shouldFailWithUpdateMessageIfUserNotFound() {
		String userId = UUID.randomUUID().toString();
		MessageInfo msg = insertMessage(userId, "First Message");

		//
		// Try to update message
		//
		webTestClient.put()
				.uri("/message/" + msg.getMsgId())
				.header("X-UserId", "NON-EXISING-USER")
				.bodyValue(new Message().setMessage("Message update"))
				.exchange()
				.expectStatus()
				.isNotFound();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(1);
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getMessage()).isEqualTo("First Message");
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg.getMsgId()).getUserId()).isEqualTo(userId);
	}

	@Test
	void shouldDeleteMessage() {
		String userId = UUID.randomUUID().toString();
		MessageInfo msg1 = insertMessage(userId, "First Message");
		MessageInfo msg2 = insertMessage(userId, "Second Message");

		//
		// Verify message is updated
		//
		webTestClient.delete()
				.uri("/message/" + msg1.getMsgId())
				.header("X-UserId", userId)
				.exchange()
				.expectStatus()
				.is2xxSuccessful();

		assertThat(messageRepositoryService.getMessageRepo().containsKey(userId)).isTrue();
		assertThat(messageRepositoryService.getMessageRepo().get(userId).size()).isEqualTo(1);
		assertThat(messageRepositoryService.getMessageRepo().get(userId).get(msg2.getMsgId()).getMessage()).isEqualTo("Second Message");
	}

	@Test
	void shouldGetMessages() {
		String userId1 = UUID.randomUUID().toString();
		String userId2 = UUID.randomUUID().toString();
		insertMessage(userId1, "User1 - First Message");
		insertMessage(userId1, "User1 - Second Message");
		insertMessage(userId2, "User2 - First Message");
		insertMessage(userId2, "User2 - Second Message");

		//
		// Verify get messages
		//
		List<MessageInfo> result = webTestClient.get()
				.uri("/message/")
				.header("X-UserId", userId1)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.returnResult(MessageInfo.class)
				.getResponseBody()
				.collectList()
				.block();

 	 	assertThat(result.size()).isEqualTo(4);
	}


	private MessageInfo insertMessage(String userId, String message) {
		String msgId = UUID.randomUUID().toString();

		if (!messageRepositoryService.getMessageRepo().containsKey(userId)) {
			messageRepositoryService.getMessageRepo().put(userId, new HashMap<>());
		}
		MessageInfo messageInfo = new MessageInfo()
				.setMsgId(msgId)
				.setUserId(userId)
				.setMessage(message)
				.setDate(utils.getCurrentDate())
				.setTimestamp(Instant.now().getEpochSecond());

		messageRepositoryService.getMessageRepo().get(userId).put(msgId,messageInfo);

		return messageInfo;
	}
}
