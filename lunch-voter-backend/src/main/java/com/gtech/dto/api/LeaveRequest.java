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
public class LeaveRequest {

	@ApiModelProperty(required=true, value="Code of voting session required to leave.")
	@NotBlank(message = "{vote.leave.error.code-must-be-provided}")
	private String code;

	@ApiModelProperty(required=true, value="Code of participated user required to leave.")
	@NotBlank(message = "{vote.leave.error.user-code-must-be-provided}")
	private String userCode;
}