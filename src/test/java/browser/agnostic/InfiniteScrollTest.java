package browser.agnostic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InfiniteScrollTest {

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java";
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL + "/index.html");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void infiniteScrollTest() {
        driver.navigate().to(BASE_URL + "/infinite-scroll.html");
        By paragraphLocator = By.xpath("//p");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(paragraphLocator, 0));
        int scrollNumber = 3;

        for (int i=0; i<scrollNumber; i++) {
            int paragraphSize = driver.findElements(paragraphLocator).size();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Assertions.assertTrue(wait.until(ExpectedConditions
                            .not(ExpectedConditions.numberOfElementsToBe(paragraphLocator, paragraphSize))),
                    "New paragraphs are no longer loading");
        }
    }
}
