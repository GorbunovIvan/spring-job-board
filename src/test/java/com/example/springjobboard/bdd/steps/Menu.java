package com.example.springjobboard.bdd.steps;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class Menu {

    public void clickOnMenuItemWithText(String text) {
        $(By.xpath("//div[@id='menu']//a[text()='" + text + "']")).click();
    }
}
