package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.javatuples.Pair;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private String baseURL;
    private HomePage homePage;
    private ResultPage resultPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        NotesTests.driver = new ChromeDriver();
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + this.port;

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signupUser("luke", "skywalker", "jedi", "theforce");

        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser("jedi", "theforce");

        driver.get(baseURL + "/home");
        homePage = new HomePage(driver, new WebDriverWait(driver, 10));
    }

    @AfterEach
    public void afterEach() {
        // only one note is created in each test
        try {
            homePage.deleteFirstNote();
        } catch (NoSuchElementException ex) {

        }
    }

    @AfterAll
    static void afterAll() {
        if (NotesTests.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addNoteAndVerifyPresentShouldSucceed() {
        addNoteAndGoToNotesTab("Title for note","Description for note" );

        Pair<WebElement, WebElement> noteDetails = findNoteDetails();

        Assertions.assertEquals("Title for note", noteDetails.getValue0().getText());
        Assertions.assertEquals("Description for note", noteDetails.getValue1().getText());
    }

    @Test
    public void editNoteAndVerifyIsEditedCorrectlyShouldSucceed() {
        addNoteAndGoToNotesTab("Title for note","Description for note" );

        editNoteAndGoToNotesTab("Changed title", "Changed description");

        Pair<WebElement, WebElement> noteDetails = findNoteDetails();

        Assertions.assertEquals("Changed title", noteDetails.getValue0().getText());
        Assertions.assertEquals("Changed description", noteDetails.getValue1().getText());
    }

    @Test
    public void deleteNoteAndVerifyIsNoLongerDisplayedShouldSucceed() {
        addNoteAndGoToNotesTab("Title for note","Description for note" );

        deleteNoteAndGoToNotesTab();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Pair<WebElement, WebElement> noteDetails = findNoteDetails();
        });
    }

    private void addNoteAndGoToNotesTab(String title, String description) {
        homePage.clickNotesTab();
        homePage.addNote(title, description );

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickNotesTab();
    }

    private void editNoteAndGoToNotesTab(String title, String description) {
        homePage.editNote(title, description);

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickNotesTab();
    }

    private void deleteNoteAndGoToNotesTab() {
        homePage.deleteFirstNote();

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickNotesTab();
    }

    private Pair<WebElement, WebElement> findNoteDetails() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"))));
        WebElement noteTitleText = wait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th")));
        WebElement noteDescriptionText = wait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[2]")));

        return new Pair<WebElement, WebElement>(noteTitleText, noteDescriptionText);
    }
}