package com.attsw.library.management.integration;

import com.attsw.library.management.entity.Member;
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
        Member memberToCreate = new Member(null, "Avan Avi", "avan.avi@email.com");
        
        ResponseEntity<Member> response = restTemplate.postForEntity("/members", memberToCreate, Member.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Member savedMember = response.getBody();
        assertNotNull(savedMember);
        assertNotNull(savedMember.getId());
        assertTrue(savedMember.getId() > 0);
        assertEquals("Avan Avi", savedMember.getName());
        assertEquals("avan.avi@email.com", savedMember.getEmail());
    }

    @Test
    void testGetMemberByIdEndpoint() {
        // INTEGRATION VALIDATION 
        Member memberToCreate = new Member(null, "John Doe", "john.doe@email.com");
        ResponseEntity<Member> createResponse = restTemplate.postForEntity("/members", memberToCreate, Member.class);
        Long memberId = createResponse.getBody().getId();
        
        ResponseEntity<Member> getResponse = restTemplate.getForEntity("/members/" + memberId, Member.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Member retrievedMember = getResponse.getBody();
        assertNotNull(retrievedMember);
        assertEquals(memberId, retrievedMember.getId());
        assertEquals("John Doe", retrievedMember.getName());
        assertEquals("john.doe@email.com", retrievedMember.getEmail());
    }

    @Test
    void testGetMemberByIdWhenNotFound() {
        // INTEGRATION VALIDATION 
        
        ResponseEntity<Member> response = restTemplate.getForEntity("/members/999", Member.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllMembersEndpoint() {
        // Create multiple members first
        Member member1 = new Member(null, "Avan Avi", "avan.avi@email.com");
        Member member2 = new Member(null, "John Doe", "john.doe@email.com");
        
        restTemplate.postForEntity("/members", member1, Member.class);
        restTemplate.postForEntity("/members", member2, Member.class);
        
        // Test GET all members 
        ResponseEntity<Member[]> response = restTemplate.getForEntity("/members", Member[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Member[] members = response.getBody();
        assertNotNull(members);
        assertEquals(2, members.length);
        
        // Verify both members are returned 
        boolean foundAvan = false;
        boolean foundJohn = false;
        for (Member member : members) {
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
        Member memberToCreate = new Member(null, "Test Member", "test@email.com");
        ResponseEntity<Member> createResponse = restTemplate.postForEntity("/members", memberToCreate, Member.class);
        Long memberId = createResponse.getBody().getId();
        
        // Verify member exists
        ResponseEntity<Member> getResponse = restTemplate.getForEntity("/members/" + memberId, Member.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        
        // Delete the member 
        restTemplate.delete("/members/" + memberId);
        
        
        ResponseEntity<Member> getDeletedResponse = restTemplate.getForEntity("/members/" + memberId, Member.class);
        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }
}