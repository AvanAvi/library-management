package com.attsw.library.management.e2e;

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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers  
class MemberE2ETest {

    @Container  
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")  
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCompleteMemberWorkflow() {
        MemberDto newMember = new MemberDto(null, "John Doe", "john.doe@email.com", new ArrayList<>());
        
        ResponseEntity<MemberDto> createResponse = restTemplate.postForEntity("/members", newMember, MemberDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    }

    @Test 
    void testMemberValidationFailure() {
        
        MemberDto invalidMember = new MemberDto(null, null, null, new ArrayList<>()); 
        
        ResponseEntity<MemberDto> response = restTemplate.postForEntity("/members", invalidMember, MemberDto.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}