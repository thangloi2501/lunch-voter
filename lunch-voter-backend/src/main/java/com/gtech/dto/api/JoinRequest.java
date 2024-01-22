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
public class JoinRequest {

	@ApiModelProperty(required=true, value="Name of user to join session.")
	@NotBlank(message = "{vote.join.error.name-must-be-provided}")
	private String name;

	@ApiModelProperty(required=true, value="Code of voting session required to join.")
	@NotBlank(message = "{vote.join.error.code-must-be-provided}")
	private String code;

}