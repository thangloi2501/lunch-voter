package com.gtech.model;

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
public class CreateRequest {

	@ApiModelProperty(required=true, value="Session creator name")
	@NotBlank(message = "{vote.create.error.name-must-be-provided}")
	private String name;
}