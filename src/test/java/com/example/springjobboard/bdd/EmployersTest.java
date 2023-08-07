package com.example.springjobboard.bdd;

import com.codeborne.selenide.Condition;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class EmployersTest {

    private static Set<Employer> EMPLOYERS;

    @BeforeAll
    public static void init(@Autowired EmployerRepository employerRepository) {
        EMPLOYERS = employerRepository.findAll();
    }

    @Test
    public void employersPage() {

        open("http://localhost:8080/employers");

        $(By.xpath("//h4[text()='Employers']")).shouldBe(Condition.visible);

        for (var employer : EMPLOYERS) {
            $(By.xpath("//a[text()='" + employer.getName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//a[text()='" + employer.getName() + "']")).click();
            $(By.xpath("//h4[text()='Employer']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + employer.getName() + "']")).shouldBe(Condition.visible);
            back();
        }
    }

    @Test
    public void employerPage() {

        for (var employer : EMPLOYERS) {

            open("http://localhost:8080/employers/" + employer.getId());
            $(By.xpath("//h4[text()='Employer']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + employer.getName() + "']")).shouldBe(Condition.visible);

            // vacancies
            $(By.xpath("//p[text()='Vacancies:']")).shouldBe(Condition.visible);
            for (var vacancy : employer.getVacancies()) {
                $(By.xpath("//a[text()='" + vacancy.getTitle() + "']")).shouldBe(Condition.visible);
                $(By.xpath("//a[text()='" + vacancy.getTitle() + "']")).click();
                $(By.xpath("//h4[text()='Vacancy']")).shouldBe(Condition.visible);
                back();
            }
        }
    }
}
