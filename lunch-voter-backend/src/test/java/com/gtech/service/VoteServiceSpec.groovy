package com.gtech.service

import com.gtech.config.TestEnvironmentConfiguration
import com.gtech.db.entity.UserVote
import com.gtech.db.entity.VoteSession
import com.gtech.db.repository.UserVoteRepo
import com.gtech.db.repository.VoteSessionRepo
import com.gtech.exception.ApiException
import com.gtech.dto.api.CreateRequest
import com.gtech.dto.api.JoinRequest
import org.spockframework.spring.SpringBean
import org.spockframework.spring.SpringSpy
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpStatus
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

import static com.gtech.utils.VoteUtils.TOPIC_VOTE

@ContextConfiguration(classes = [TestEnvironmentConfiguration.class, VoteService.class])
@PropertySource(value="classpath:application.yaml")
class VoteServiceSpec extends Specification {
    @SpringSpy
    VoteService voteService

    @SpringBean
    UserVoteRepo userVoteRepo = Mock()

    @SpringBean
    VoteSessionRepo voteSessionRepo = Mock()

    @SpringBean
    SimpMessagingTemplate simpMessagingTemplate = Mock()

    def userVote = Mock(UserVote.class) {
        getUserCode() >> 'dummy-userCode'
        getName() >> 'dummy-name'
        getVoteValue() >> 'dummy-voteValue'
        getCreatedAt() >> LocalDateTime.now()
        getVoteSessionId() >> 1L
    }

    def voteSession = Mock(VoteSession.class) {
        getId() >> 1L
        getCode() >> 'dummy-code'
        getCreatorCode() >> 'dummy-usercode'
        getUserVotes() >> [userVote]
    }


    def "Create session should succeed"() {
        given:
        def request = [
                name  : 'user-1'
        ] as CreateRequest

        when:
        def result = voteService.createSession(request)

        then:
        1 * voteSessionRepo.save(_) >> voteSession
        1 * userVoteRepo.save(_)

        then: 'result as expected'
        result.code != ''
        result.userCode != ''
    }

    def "Join session should throw error if session code not found"() {
        given:
        def request = [
                name: 'user-1',
                code: 'invalid-code'
        ] as JoinRequest

        when:
        def result = voteService.joinSession(request)

        then:
        1 * voteSessionRepo.findOneByCode(_) >> Optional.empty()

        ApiException exception = thrown()
        exception.status == HttpStatus.NOT_FOUND
    }

    def "Join session should succeed"() {
        given:
        def request = [
                name: 'user-1',
                code: 'dummy-code'
        ] as JoinRequest

        when:
        def result = voteService.joinSession(request)

        then:
        1 * voteSessionRepo.findOneByCode(_) >> Optional.of(voteSession)
        1 * userVoteRepo.saveAndFlush(_)
        2 * simpMessagingTemplate.convertAndSend(String.format("%s/%s", TOPIC_VOTE, "dummy-code"), _)
    }

    //TODO: Add more unit tests here
}
