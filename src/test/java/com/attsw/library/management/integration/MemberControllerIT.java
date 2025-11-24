package com.attsw.library.management.integration;

import com.attsw.library.management.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class MemberControllerIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateMemberEndpoint() {
        MemberDto memberToCreate = new MemberDto(null, "Avan Avi", "avan.avi@email.com", new ArrayList<>());

        ResponseEntity<MemberDto> response = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetMemberByIdEndpoint() {
        MemberDto memberToCreate = new MemberDto(null, "John Doe", "john.doe@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);
        Long memberId = createResponse.getBody().getId();

        ResponseEntity<MemberDto> getResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(memberId, getResponse.getBody().getId());
    }

    @Test
    void testGetMemberByIdWhenNotFound() {
        ResponseEntity<MemberDto> response = restTemplate.getForEntity("/members/999", MemberDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllMembersEndpoint() {
        MemberDto member1 = new MemberDto(null, "Avan Avi", "avan.avi@email.com", new ArrayList<>());
        MemberDto member2 = new MemberDto(null, "John Doe", "john.doe@email.com", new ArrayList<>());

        restTemplate.postForEntity("/members", member1, MemberDto.class);
        restTemplate.postForEntity("/members", member2, MemberDto.class);

        ResponseEntity<MemberDto[]> response = restTemplate.getForEntity("/members", MemberDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testDeleteMemberEndpoint() {
        MemberDto memberToCreate = new MemberDto(null, "Test Member", "test@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);
        Long memberId = createResponse.getBody().getId();

        ResponseEntity<MemberDto> getResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        restTemplate.delete("/members/" + memberId);

        ResponseEntity<MemberDto> getDeletedResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }

    @Test
    void testUpdateMemberEndpoint() {
        MemberDto originalMember = new MemberDto(null, "Original Name", "original@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", originalMember, MemberDto.class);
        Long memberId = createResponse.getBody().getId();

        MemberDto updatedMember = new MemberDto(memberId, "Updated Name", "updated@email.com", new ArrayList<>());

        ResponseEntity<MemberDto> updateResponse = restTemplate.exchange(
            "/members/" + memberId,
            HttpMethod.PUT,
            new HttpEntity<>(updatedMember),
            MemberDto.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(memberId, updateResponse.getBody().getId());
    }
}
