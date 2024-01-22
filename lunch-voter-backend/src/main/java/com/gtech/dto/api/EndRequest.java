package com.gtech.dto.api;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class EndRequest {

	@NotBlank(message = "{vote.end.error.code-must-be-provided}")
	private String code;

	@NotBlank(message = "{vote.end.error.user-code-must-be-provided}")
	private String userCode;
}