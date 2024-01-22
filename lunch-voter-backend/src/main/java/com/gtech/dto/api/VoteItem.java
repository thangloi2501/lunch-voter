package com.gtech.dto.api;

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
	private LocalDateTime updatedAt;
}