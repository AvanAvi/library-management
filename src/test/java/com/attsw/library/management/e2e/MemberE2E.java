package com.attsw.library.management.e2e;

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
class MemberE2E {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCompleteMemberWorkflow() {
        
        Member newMember = new Member(null, "John Doe", "john.doe@email.com");
        
        ResponseEntity<Member> createResponse = restTemplate.postForEntity("/members", newMember, Member.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    }

    @Test 
    void testMemberValidationFailure() {
        // RED 
        Member invalidMember = new Member(null, null, null); // All null fields
        
        ResponseEntity<Member> response = restTemplate.postForEntity("/members", invalidMember, Member.class);
        
       
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}