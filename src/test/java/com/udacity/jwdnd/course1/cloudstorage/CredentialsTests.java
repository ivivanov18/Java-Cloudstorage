package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.tuple.Triple;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsTests {
    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private String baseURL;
    private HomePage homePage;
    private ResultPage resultPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        CredentialsTests.driver = new ChromeDriver();
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
        // only one credential is created in each test
        try {
            homePage.deleteFirstCredential();
        } catch (NoSuchElementException ex) {

        }
    }

    @AfterAll
    static void afterAll() {
        if (CredentialsTests.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void addCredentialAndVerifyPresentShouldSucceed() {
        addCredentialAndGoToCredentialsTab("http://localhost:8080", "jedi", "theforce");

        Triplet<WebElement, WebElement, WebElement> credentialDetails = findCredentialDetails();

        Assertions.assertEquals("http://localhost:8080", credentialDetails.getValue0().getText());
        Assertions.assertEquals("jedi", credentialDetails.getValue1().getText());
    }

    @Test
    public void editCredentialAndVerifyIsEditedCorrectlyShouldSucceed() {
        addCredentialAndGoToCredentialsTab("http://localhost:8080", "jedi", "theforce");

        editCredentialAndGoToCredentialsTab("http://localhost:4444", "jediisback", "theforce1");

        Triplet<WebElement, WebElement, WebElement> credentialDetails = findCredentialDetails();

        Assertions.assertEquals("http://localhost:4444", credentialDetails.getValue0().getText());
        Assertions.assertEquals("jediisback", credentialDetails.getValue1().getText());
    }

    @Test
    public void deleteCredentialAndVerifyIsNoLongerDisplayedShouldSucceed() {
        addCredentialAndGoToCredentialsTab("http://localhost:8080", "jedi", "theforce");

        deleteCredentialAndGoToNotesTab();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Triplet<WebElement, WebElement, WebElement> noteDetails = findCredentialDetails();
        });
    }

    private void addCredentialAndGoToCredentialsTab(String url, String username, String password) {
        homePage.clickCredentialsTab();
        homePage.addCredential(url, username, password );

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickCredentialsTab();
    }

    private void editCredentialAndGoToCredentialsTab(String url, String username, String password) {
        homePage.editCredential(url, username, password);

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickCredentialsTab();
    }
    private void deleteCredentialAndGoToNotesTab() {
        homePage.deleteFirstCredential();

        resultPage = new ResultPage(driver);
        resultPage.clickHereToContinueLink();

        homePage.clickCredentialsTab();
    }


    private Triplet findCredentialDetails() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"))));
        WebElement credentialUrlText = wait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th")));
        WebElement credentialUsernameText = wait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[2]")));
        WebElement credentialPasswordText = wait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]")));

        return new Triplet(credentialUrlText, credentialUsernameText, credentialPasswordText);
    }
}
