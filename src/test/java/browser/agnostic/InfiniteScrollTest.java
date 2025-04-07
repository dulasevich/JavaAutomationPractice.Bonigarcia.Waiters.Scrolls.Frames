package browser.agnostic;

import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InfiniteScrollTest {

    @Test
    void infiniteScrollTest() {
        WebDriver driver = new ChromeDriver();
        TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driver.findElement(By.xpath("//a[@href='infinite-scroll.html']")).click();

        By paragraphLocator = By.xpath("//p");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getShortTimeout()));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(paragraphLocator, 0));
        int scrollNumber = 3;

        for (int i = 0; i < scrollNumber; i++) {
            int paragraphSize = driver.findElements(paragraphLocator).size();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Assertions.assertTrue(wait.until(ExpectedConditions
                            .not(ExpectedConditions.numberOfElementsToBe(paragraphLocator, paragraphSize))),
                    "New paragraphs are no longer loading");
        }

        driver.quit();
    }
}
