package com.example.springjobboard.bdd.pureSelenium;

import com.codeborne.selenide.Condition;
import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class VacanciesTest {

    private static Set<Vacancy> VACANCIES;

    @BeforeAll
    public static void init(@Autowired VacancyRepository vacancyRepository) {
        VACANCIES = vacancyRepository.findAll();
    }

    @Test
    public void vacanciesPage() {

        open("http://localhost:8080/vacancies");

        $(By.xpath("//h4[text()='Vacancies']")).shouldBe(Condition.visible);

        for (var vacancy : VACANCIES) {
            $(By.xpath("//a[text()='" + vacancy.getTitle() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//a[text()='" + vacancy.getTitle() + "']")).click();
            $(By.xpath("//h4[text()='Vacancy']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + vacancy.getTitle() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + vacancy.getDescription() + "']")).shouldBe(Condition.visible);
            back();
        }
    }

    @Test
    public void vacancyPage() {

        for (var vacancy : VACANCIES) {

            open("http://localhost:8080/vacancies/" + vacancy.getId());
            $(By.xpath("//h4[text()='Vacancy']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + vacancy.getTitle() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//span[text()='" + vacancy.getDescription() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//p[contains(text(), 'By employer')]")).shouldBe(Condition.visible);

            // employer link
            $(By.xpath("//p//a[text()='" + vacancy.getEmployer().getName() + "']")).shouldBe(Condition.visible);
            $(By.xpath("//p//a[text()='" + vacancy.getEmployer().getName() + "']")).click();
            $(By.xpath("//h4[text()='Employer']")).shouldBe(Condition.visible);
            back();
        }
    }
}
