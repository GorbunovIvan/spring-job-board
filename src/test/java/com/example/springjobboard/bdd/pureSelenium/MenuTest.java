package com.example.springjobboard.bdd.pureSelenium;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@SpringBootTest
public class MenuTest {

    @Test
    public void menus() {

        open("http://localhost:8080");

        // vacancies
        $(By.xpath("//div[@id='menu']//a[text()='Vacancies']")).click();
        $(By.xpath("//h4[text()='Vacancies']")).shouldBe(Condition.visible);

        // employers
        $(By.xpath("//div[@id='menu']//a[text()='Employers']")).click();
        $(By.xpath("//h4[text()='Employers']")).shouldBe(Condition.visible);

        // applicants
        $(By.xpath("//div[@id='menu']//a[text()='Applicants']")).click();
        $(By.xpath("//h4[text()='Applicants']")).shouldBe(Condition.visible);

        // login
        $(By.xpath("//div[@id='menu']//a[text()='Login']")).click();
        $(By.xpath("//h4[text()='Log in']")).shouldBe(Condition.visible);

        // register
        open("http://localhost:8080");
        $(By.xpath("//div[@id='menu']//a[text()='Register']")).click();
        $(By.xpath("//h4[text()='Registration']")).shouldBe(Condition.visible);
    }
}
