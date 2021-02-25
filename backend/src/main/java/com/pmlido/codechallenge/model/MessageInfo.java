package com.pmlido.codechallenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageInfo {
	private String msgId;
	private String message;
	private String userId;
	private String date;
	private Long timestamp;
}
