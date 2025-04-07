package pom;

import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(config.getShortTimeout()));
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driver.findElement(By.xpath("//a[@href='login-form.html']")).click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void successLoginTest() {
        login(config.getUsername(), config.getPassword());
        By successAlertXpath = By.xpath("//div[@id='success']");

        Assertions.assertAll(
                () -> Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(successAlertXpath)).isDisplayed(),
                        "Login success alert is NOT displayed"),
                () -> Assertions.assertEquals("Login successful", driver.findElement(successAlertXpath).getText(),
                        "Success message is wrong")
        );
    }

    @Test
    void invalidLoginTest() {
        login(config.getUsername(), "wrongPassword");
        By invalidCredentialsAlert = By.xpath("//div[@id='invalid']");

        Assertions.assertAll(
                () -> Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(invalidCredentialsAlert)).isDisplayed(),
                        "Login success alert is NOT displayed"),
                () -> Assertions.assertEquals("Invalid credentials", driver.findElement(invalidCredentialsAlert).getText(),
                        "Success message is wrong")
        );
    }

    private void login(String username, String password) {
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button")).click();
    }
}
