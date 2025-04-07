package browser.agnostic;

import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class IFramesTest {
    private WebDriver driver;
    private final TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driver.findElement(By.xpath("//a[@href='iframes.html']")).click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void iFramesNegativeTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.xpath("//p")));
    }

    @Test
    void iFramesTest() throws IOException {
        By paragraphLocator = By.xpath("//p");
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(paragraphLocator));

        driver.switchTo().frame(driver.findElement(By.xpath("//iframe")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Boolean isPageScrolledToBottom = (Boolean) new WebDriverWait(driver, Duration.ofSeconds(config.getShortTimeout())).until(ExpectedConditions
                .jsReturnsValue("return window.scrollY + window.innerHeight >= document.body.scrollHeight;"));
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src/test/resources/image.png"));
        Assertions.assertTrue(isPageScrolledToBottom);

        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//a[@href='https://bonigarcia.dev/']")).click();
        Assertions.assertEquals("https://bonigarcia.dev/", driver.getCurrentUrl());
    }
}
