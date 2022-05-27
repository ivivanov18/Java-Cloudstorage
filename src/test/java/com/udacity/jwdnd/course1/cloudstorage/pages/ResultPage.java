package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    private static WebDriver driver;

    @FindBy(tagName = "a")
    private WebElement hereToContinueLink;

    public ResultPage(WebDriver driver) {
        ResultPage.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickHereToContinueLink() {
        hereToContinueLink.click();
    }
}
