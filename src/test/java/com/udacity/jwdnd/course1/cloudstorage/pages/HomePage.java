package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    private WebElement addNoteButton;
    private WebElement noteTitleInput;
    private WebElement noteDescriptionInput;
    private WebElement saveNoteChangesButton;
    private WebElement addCredentialButton;
    private WebElement credentialUrlInput;
    private WebElement credentialUsernameInput;
    private WebElement credentialPasswordInput;
    private WebElement saveCredentialChangesButton;

    final private WebDriver driver;
    final private WebDriverWait wait;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        wait.until(webDriver -> webDriver.findElement(By.id("logout-button")));
        logoutButton.click();
    }

    public void addNote(String title, String description) {
        clickNotesTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-button")));
        addNoteButton = driver.findElement(By.id("add-note-button"));
        addNoteButton.click();

        fillNoteData(title, description);

        saveNoteChangesButton = driver.findElement(By.id("save-note-button"));
        saveNoteChangesButton.click();
    }

    public void editNote(String title, String description) {
        clickNotesTab();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement editNoteButton = driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/button"));
        wait.until(ExpectedConditions.elementToBeClickable(editNoteButton));
        editNoteButton.click();

        fillNoteData(title, description);

        saveNoteChangesButton = driver.findElement(By.id("save-note-button"));
        saveNoteChangesButton.click();
    }

    public void editCredential(String url, String username, String password) {
        clickCredentialsTab();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement editCredentialButton = driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/button"));
        wait.until(ExpectedConditions.elementToBeClickable(editCredentialButton));
        editCredentialButton.click();

        fillCredentialData(url, username, password);

        saveCredentialChangesButton = driver.findElement(By.id("save-credential-button"));
        saveCredentialChangesButton.click();
    }

    public void deleteFirstNote() {
        clickNotesTab();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement deleteNoteLink = driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/a"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteNoteLink));

        deleteNoteLink.click();
    }

    public void deleteFirstCredential() {
        clickCredentialsTab();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement deleteCredentialLink = driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/a"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteCredentialLink));

        deleteCredentialLink.click();
    }

    public void addCredential(String url, String username, String password) {
        clickCredentialsTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        addCredentialButton = driver.findElement(By.id("add-credential-button"));
        addCredentialButton.click();

        fillCredentialData(url, username, password);

        saveCredentialChangesButton = driver.findElement(By.id("save-credential-button"));
        saveCredentialChangesButton.click();
    }

    public void clickNotesTab() {
        clickTab("nav-notes-tab");
    }

    public void clickCredentialsTab() {
        clickTab("nav-credentials-tab");
    }

    public void clickTab(String tabId) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement tab = driver.findElement(By.id(tabId));
        wait.until(ExpectedConditions.elementToBeClickable(tab));
        tab.click();
    }

    private void fillNoteData(String title, String description) {
        noteTitleInput = driver.findElement(By.id("note-title"));
        wait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(title);
        noteDescriptionInput = driver.findElement(By.id("note-description"));
        wait.until(ExpectedConditions.visibilityOf(noteDescriptionInput));
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(description);
    }

    private void fillCredentialData(String url, String username, String password) {
        credentialUrlInput = driver.findElement(By.id("credential-url"));
        wait.until(ExpectedConditions.visibilityOf(credentialUrlInput));
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);

        credentialUsernameInput = driver.findElement(By.id("credential-username"));
        wait.until(ExpectedConditions.visibilityOf(credentialUsernameInput));
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);

        credentialPasswordInput = driver.findElement(By.id("credential-password"));
        wait.until(ExpectedConditions.visibilityOf(credentialPasswordInput));
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);
    }
}
