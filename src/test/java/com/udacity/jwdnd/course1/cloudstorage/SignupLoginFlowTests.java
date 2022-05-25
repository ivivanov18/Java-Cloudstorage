package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupLoginFlowTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private String baseURL;

    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + this.port;
    }

    @AfterAll
    static void afterAll() {
        if (SignupLoginFlowTests.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void accessToPagesWithoutLoggingShouldRedirectToLoginPage() {
        String[] pages = {"/credentials", "/notes", "/home"};

        for (String page: pages) {
            driver.get(baseURL + page);
            Assertions.assertEquals("Login", driver.getTitle());
        }
    }

    @Test
    public void accessingSignupWithoutLoggingShouldBePossible() {
        driver.get(baseURL + "/signup");
        Assertions.assertEquals("Signup", driver.getTitle());
    }

    @Test
    public void homePageIsAccessibleForLoggedUserAndNotOnceLoggedOut () {
        signupUser("luke", "skywalker", "jedi", "theforce");
        Assertions.assertEquals("Login", driver.getTitle());

        loginUser("jedi", "theforce");
        Assertions.assertEquals("Home", driver.getTitle());

        logoutUser();
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get(baseURL + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    private void signupUser(String firstName, String lastName, String userName, String password) {
        driver.get(baseURL + "/signup");
        signupPage = new SignupPage(driver);
        signupPage.signupUser(firstName, lastName, userName, password);
    }

    private void loginUser(String userName, String password) {
        driver.get(baseURL + "/login");
        loginPage = new LoginPage(driver);
        loginPage.loginUser(userName, password);
    }

    private void logoutUser() {
        driver.get(baseURL + "/home");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        homePage = new HomePage(driver, wait);
        homePage.logout();
    }
}
