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
        // INTEGRATION VALIDATION 
        MemberDto memberToCreate = new MemberDto(null, "Avan Avi", "avan.avi@email.com", new ArrayList<>());
        
        ResponseEntity<MemberDto> response = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        MemberDto savedMember = response.getBody();
        assertNotNull(savedMember);
        assertNotNull(savedMember.getId());
        assertTrue(savedMember.getId() > 0);
        assertEquals("Avan Avi", savedMember.getName());
        assertEquals("avan.avi@email.com", savedMember.getEmail());
    }

    @Test
    void testGetMemberByIdEndpoint() {
        // INTEGRATION VALIDATION 
        MemberDto memberToCreate = new MemberDto(null, "John Doe", "john.doe@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);
        Long memberId = createResponse.getBody().getId();
        
        ResponseEntity<MemberDto> getResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        MemberDto retrievedMember = getResponse.getBody();
        assertNotNull(retrievedMember);
        assertEquals(memberId, retrievedMember.getId());
        assertEquals("John Doe", retrievedMember.getName());
        assertEquals("john.doe@email.com", retrievedMember.getEmail());
    }

    @Test
    void testGetMemberByIdWhenNotFound() {
        // INTEGRATION VALIDATION 
        
        ResponseEntity<MemberDto> response = restTemplate.getForEntity("/members/999", MemberDto.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllMembersEndpoint() {
        // Create multiple members first
        MemberDto member1 = new MemberDto(null, "Avan Avi", "avan.avi@email.com", new ArrayList<>());
        MemberDto member2 = new MemberDto(null, "John Doe", "john.doe@email.com", new ArrayList<>());
        
        restTemplate.postForEntity("/members", member1, MemberDto.class);
        restTemplate.postForEntity("/members", member2, MemberDto.class);
        
        // Test GET all members 
        ResponseEntity<MemberDto[]> response = restTemplate.getForEntity("/members", MemberDto[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MemberDto[] members = response.getBody();
        assertNotNull(members);
        assertEquals(2, members.length);
        
        // Verify both members are returned 
        boolean foundAvan = false;
        boolean foundJohn = false;
        for (MemberDto member : members) {
            if ("Avan Avi".equals(member.getName())) {
                foundAvan = true;
                assertEquals("avan.avi@email.com", member.getEmail());
            } else if ("John Doe".equals(member.getName())) {
                foundJohn = true;
                assertEquals("john.doe@email.com", member.getEmail());
            }
        }
        assertTrue(foundAvan, "Avan Avi member should be found");
        assertTrue(foundJohn, "John Doe member should be found");
    }

    @Test
    void testDeleteMemberEndpoint() {
        // Create a member 
        MemberDto memberToCreate = new MemberDto(null, "Test Member", "test@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", memberToCreate, MemberDto.class);
        Long memberId = createResponse.getBody().getId();
        
        // Verify member exists
        ResponseEntity<MemberDto> getResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        
        // Delete the member 
        restTemplate.delete("/members/" + memberId);
        
        
        ResponseEntity<MemberDto> getDeletedResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }
    
    @Test
    void testUpdateMemberEndpoint() {
        // INTEGRATION VALIDATION - PUT endpoint
        
        // First create a member
        MemberDto originalMember = new MemberDto(null, "Original Name", "original@email.com", new ArrayList<>());
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", originalMember, MemberDto.class);
        Long memberId = createResponse.getBody().getId();
        
        // 2nd update the member
        MemberDto updatedMember = new MemberDto(memberId, "Updated Name", "updated@email.com", new ArrayList<>());
        
        ResponseEntity<MemberDto> updateResponse = restTemplate.exchange(
            "/members/" + memberId,
            HttpMethod.PUT,
            new HttpEntity<>(updatedMember),
            MemberDto.class
        );
        
        // Verify update response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        MemberDto responseMember = updateResponse.getBody();
        assertNotNull(responseMember);
        assertEquals(memberId, responseMember.getId());
        assertEquals("Updated Name", responseMember.getName());
        assertEquals("updated@email.com", responseMember.getEmail());
        
        // Verify the member was actually updated in database
        ResponseEntity<MemberDto> getResponse = restTemplate.getForEntity("/members/" + memberId, MemberDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        MemberDto retrievedMember = getResponse.getBody();
        assertEquals("Updated Name", retrievedMember.getName());
        assertEquals("updated@email.com", retrievedMember.getEmail());
    }
    
}