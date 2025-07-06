package com.attsw.library.management.bdd;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.MemberService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberManagementSteps {

    @Autowired
    private MemberService memberService;

    private Member member;
    private Member savedMember;
    private List<Member> memberList;

    @Given("I have a member with name {string} and email {string}")
    public void i_have_a_member_with_name_and_email(String name, String email) {
        member = new Member(null, name, email);
    }

    @When("I register the member")
    public void i_register_the_member() {
        savedMember = memberService.saveMember(member);
    }

    @Then("the member should be registered with an ID")
    public void the_member_should_be_registered_with_an_id() {
        assertNotNull(savedMember);
        assertNotNull(savedMember.getId());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getEmail(), savedMember.getEmail());
    }

    @Given("a member exists with ID {int}")
    public void a_member_exists_with_id(Integer id) {
        member = new Member(Long.valueOf(id), "Test Member", "test@email.com");
        savedMember = memberService.saveMember(member);
    }

    @When("I search for the member by ID {int}")
    public void i_search_for_the_member_by_id(Integer id) {
        savedMember = memberService.findById(Long.valueOf(id));
    }

    @Then("I should receive the member details")
    public void i_should_receive_the_member_details() {
        assertNotNull(savedMember);
        assertEquals("Test Member", savedMember.getName());
        assertEquals("test@email.com", savedMember.getEmail());
    }

    @Given("there are members in the library")
    public void there_are_members_in_the_library() {
        Member member1 = new Member(null, "Member 1", "member1@email.com");
        Member member2 = new Member(null, "Member 2", "member2@email.com");
        memberService.saveMember(member1);
        memberService.saveMember(member2);
    }

    @When("I request all members")
    public void i_request_all_members() {
        memberList = memberService.findAll();
    }

    @Then("I should receive a list of members")
    public void i_should_receive_a_list_of_members() {
        assertNotNull(memberList);
        assertTrue(memberList.size() >= 2);
    }
}