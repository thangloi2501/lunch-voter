package com.gtech.dto.api;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SubmitRequest {

	@ApiModelProperty(required=true, value="Code of voting session required to submit.")
	@NotBlank(message = "{vote.submit.error.code-must-be-provided}")
	private String code;

	@ApiModelProperty(required=true, value="Code of participated user required to submit.")
	@NotBlank(message = "{vote.submit.error.user-code-must-be-provided}")
	private String userCode;

	@ApiModelProperty(required=true, value="Value of vote to submit.")
	@NotBlank(message = "{vote.submit.error.vote-must-be-provided}")
	private String voteValue;
}