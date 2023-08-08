package com.example.springjobboard.bdd.steps;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BasicPage {

    public void openURL(String url) {
        open(url);
    }

    public void clickLinkWithText(String text) {
        $(By.xpath("//a[text()='" + text + "']")).click();
    }

    public void findTagWithText(String tag, String text) {
        $(By.xpath("//" + tag + "[text()='" + text + "']")).shouldBe(Condition.visible);
    }

    public void findTagWhichContainsText(String tag, String text) {
        $(By.xpath("//" + tag + "[contains(text(), '" + text + "')]")).shouldBe(Condition.visible);
    }
}
