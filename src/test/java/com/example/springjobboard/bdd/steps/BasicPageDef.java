package com.example.springjobboard.bdd.steps;

import io.cucumber.java.en.Then;

public class BasicPageDef {

    private final BasicPage basicPage = new BasicPage();

    @Then("Open url {string}")
    public void openURL(String arg0) {
        basicPage.openURL(arg0);
    }

    @Then("Content on tag {string} with text {string} visible")
    public void hasText(String tag, String text) {
        basicPage.findTagWithText(tag, text);
    }

    @Then("Content on tag {string} containing text {string} visible")
    public void containsText(String tag, String text) {
        basicPage.findTagWhichContainsText(tag, text);
    }

    @Then("Click {string} link")
    public void clickLink(String arg0) {
        basicPage.clickLinkWithText(arg0);
    }
}
