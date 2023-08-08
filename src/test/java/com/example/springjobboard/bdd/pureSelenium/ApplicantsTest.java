package com.example.springjobboard.bdd.pureSelenium;

import com.codeborne.selenide.Condition;
import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.repository.ApplicantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class ApplicantsTest {

    private static Set<Applicant> APPLICANTS;

    @BeforeAll
    public static void init(@Autowired ApplicantRepository applicantRepository) {
        APPLICANTS = applicantRepository.findAll();
    }

    @Test
    public void applicantsPage() {

        open("http://localhost:8080/applicants");

        $(By.xpath("//h4[text()='Applicants']")).shouldBe(Condition.visible);

        for (var applicant : APPLICANTS) {
            $(By.xpath("//a[text()='" + applicant.getFullName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//a[text()='" + applicant.getFullName() + "']")).click();
            $(By.xpath("//h4[text()='Applicant']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getFirstName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getLastName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getDescription() + "']")).shouldBe(Condition.visible);
            back();
        }
    }

    @Test
    public void applicantPage() {

        for (var applicant : APPLICANTS) {

            open("http://localhost:8080/applicants/" + applicant.getId());
            $(By.xpath("//h4[text()='Applicant']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getFirstName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getLastName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + applicant.getDescription() + "']")).shouldBe(Condition.visible);
        }
    }
}
