package com.gtech.controller

import com.fasterxml.jackson.databind.ObjectWriter
import com.gtech.config.TestEnvironmentConfiguration
import com.gtech.service.VoteService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.PropertySource
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@WebMvcTest(controllers = VoteController.class)
@ContextConfiguration(classes = [
        VoteController.class,
        TestEnvironmentConfiguration.class
])
@PropertySource(value = "classpath:application.yaml")
class VoteControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectWriter objectWriter

    @SpringBean
    VoteService voteService = Mock()

    def "POST create new voting session should succeed"() {
        given:
        Map requestBody = [
                "name": "user-1"
        ]

        RequestBuilder request = MockMvcRequestBuilders.post('/api/v1/votes')
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(requestBody))

        when:
        ResultActions result = mvc.perform(request)

        then:
        1 * voteService.createSession(_)
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "POST join a voting session should succeed"() {
        given:
        Map requestBody = [
                "name": "user-2",
                "code": "dummy-code"
        ]

        RequestBuilder request = MockMvcRequestBuilders.post('/api/v1/votes/join')
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(requestBody))

        when:
        ResultActions result = mvc.perform(request)

        then:
        1 * voteService.joinSession(_)
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "PUT submit a voting value should succeed"() {
        given:
        Map requestBody = [
                "code"     : "dummy-code",
                "userCode" : "dummy-usercode",
                "voteValue": "dummy-value"
        ]

        RequestBuilder request = MockMvcRequestBuilders.put('/api/v1/votes/submit')
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(requestBody))

        when:
        ResultActions result = mvc.perform(request)

        then:
        1 * voteService.submitVote(_)
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "DELETE end a voting session should succeed"() {
        given:
        Map requestBody = [
                "code"     : "dummy-code",
                "userCode" : "dummy-usercode"
        ]

        RequestBuilder request = MockMvcRequestBuilders.delete('/api/v1/votes')
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectWriter.writeValueAsString(requestBody))

        when:
        ResultActions result = mvc.perform(request)

        then:
        1 * voteService.endSession(_)
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }
}
