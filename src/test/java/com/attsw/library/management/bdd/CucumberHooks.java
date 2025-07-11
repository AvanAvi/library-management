package com.attsw.library.management.bdd;

import com.attsw.library.management.repository.BookRepository;
import com.attsw.library.management.repository.MemberRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CucumberHooks {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void beforeScenario() {
        // Clean up database before each scenario
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @After
    public void afterScenario() {
        // Clean up database after each scenario
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }
}