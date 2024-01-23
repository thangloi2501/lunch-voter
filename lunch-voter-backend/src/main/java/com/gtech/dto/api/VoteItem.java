package com.gtech.dto.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class VoteItem {

	private String name;

	private String voteValue;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updatedAt;

	@Builder.Default
	private boolean isFinal = false;
}