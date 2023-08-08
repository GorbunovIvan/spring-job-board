package com.example.springjobboard.bdd.steps;

import io.cucumber.java.en.Then;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class MenuDef {

    private final Menu menuPage = new Menu();

    @Then("Click on menu item {string}")
    public void hasText(String text) {
        $(By.xpath("//div[@id='menu']//a[text()='" + text + "']")).click();
        menuPage.clickOnMenuItemWithText(text);
    }
}
