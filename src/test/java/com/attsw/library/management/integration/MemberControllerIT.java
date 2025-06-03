package com.attsw.library.management.integration;

import com.attsw.library.management.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateMemberEndpoint() {
        // INTEGRATION VALIDATION - Tests existing working functionality
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
        // INTEGRATION VALIDATION - Tests existing working functionality
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
        //  RED -  FAIL because MemberService returns null
        //  MemberController returns 200 OK with null body 
        ResponseEntity<Member> response = restTemplate.getForEntity("/members/999", Member.class);
        
        // Assertion FAIL - code returns 200 OK with null body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}