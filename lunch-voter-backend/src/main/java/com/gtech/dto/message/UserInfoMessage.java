package com.gtech.dto.message;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserInfoMessage implements Message {

	private String name;
	private String action;
	private LocalDateTime updatedAt;

	@Override
	public Type getType() {
		return Type.USER_INFO;
	}
}