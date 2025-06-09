package com.attsw.library.management.e2e;

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
class MemberE2E {

    @Container  
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")  
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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
        
        Member invalidMember = new Member(null, null, null); 
        
        ResponseEntity<Member> response = restTemplate.postForEntity("/members", invalidMember, Member.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}