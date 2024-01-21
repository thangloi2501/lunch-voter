package com.gtech.controller;

import com.gtech.dto.common.ApiResponse;
import com.gtech.model.CreateRequest;
import com.gtech.model.CreateResponse;
import com.gtech.model.EndRequest;
import com.gtech.model.JoinRequest;
import com.gtech.model.JoinResponse;
import com.gtech.model.SubmitRequest;
import com.gtech.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Vote")
@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {

  @Autowired
  private VoteService voteService;

  @ApiOperation(value = "Create a new voting session.")
  @PostMapping
  @ApiResponses(value = {
      @io.swagger.annotations.ApiResponse(code = 200, message = "Success with code and userCode in response."),
      @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input parameters.")})
  public ApiResponse<CreateResponse> create(
      @ApiParam(required = true, value = "Request input to create session")
      @Valid @RequestBody CreateRequest createRequest) {
    return ApiResponse.<CreateResponse>builder()
        .data(voteService.createSession(createRequest))
        .build();
  }

  @ApiOperation(value = "Join a voting session.")
  @PostMapping("/join")
  @ApiResponses(value = {
      @io.swagger.annotations.ApiResponse(code = 200, message = "Success with code and userCode in response."),
      @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input parameters."),
      @io.swagger.annotations.ApiResponse(code = 404, message = "Code or userCode not found in any voting session.")})
  public ApiResponse<JoinResponse> join(
      @ApiParam(required = true, value = "Request input to join session")
      @Valid @RequestBody JoinRequest joinRequest) {
    return ApiResponse.<JoinResponse>builder()
        .data(voteService.joinSession(joinRequest))
        .build();
  }

  //TODO: 3. don dep code 4. viet doc
  @ApiOperation(value = "Submit a vote. User can change vote value multiple times in a session.")
  @PutMapping("/submit")
  @ApiResponses(value = {
      @io.swagger.annotations.ApiResponse(code = 200, message = "Success."),
      @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input parameters."),
      @io.swagger.annotations.ApiResponse(code = 404, message = "Code or userCode not found in any voting session.")})
  public ApiResponse<Void> submit(@Valid @RequestBody SubmitRequest submitRequest) {
    voteService.submitVote(submitRequest);
    return ApiResponse.<Void>builder().build();
  }

  @ApiOperation(value = "End a voting session. Only creator is allowed to end its session.")
  @DeleteMapping
  @ApiResponses(value = {
      @io.swagger.annotations.ApiResponse(code = 200, message = "Success."),
      @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input parameters."),
      @io.swagger.annotations.ApiResponse(code = 401, message = "User is not allowed to end the session."),
      @io.swagger.annotations.ApiResponse(code = 404, message = "Code or userCode not found in any voting session.")})
  public ApiResponse<Void> end(@Valid @RequestBody EndRequest endRequest) {
    voteService.endSession(endRequest);
    return ApiResponse.<Void>builder().build();
  }
}