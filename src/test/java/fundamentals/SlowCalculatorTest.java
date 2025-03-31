package fundamentals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class SlowCalculatorTest {

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java";
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL + "/index.html");
        driver.navigate().to(BASE_URL + "/slow-calculator.html");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @ParameterizedTest
    @MethodSource("buttonsProvider")
    void calculatorTest(List<String> buttons, String result) {
        WebElement delayInput = driver.findElement(By.xpath("//input"));
        Assertions.assertEquals("5", delayInput.getDomAttribute("value"), "Default delay is wrong");

        delayInput.clear();
        delayInput.sendKeys("2");
        clickButtons(buttons);
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//span[@id = 'spinner']"))));
        WebElement resultRow = driver.findElement(By.xpath("//div[@class='screen']"));
        Assertions.assertEquals(result, resultRow.getText(), "Result is incorrect");

        driver.findElement(By.xpath("//div[@class='top']/span")).click();
        Assertions.assertEquals("", resultRow.getText(), "Result is NOT cleared");
    }

    private void clickButtons(List<String> buttons) {
        buttons.forEach(button -> driver.findElement(By.xpath(String.format("//div[@class='keys']/span[text()='%s']", button)))
                .click());
    }

    private static Stream<Arguments> buttonsProvider() {
        return Stream.of(
                Arguments.of(List.of("1", "+", "2", "="), "3"),
                Arguments.of(List.of("-", "1", "-", "2", "="), "-3"),
                Arguments.of(List.of("0", ".", "1", "x", "2", "="), "0.2"),
                Arguments.of(List.of("5", "\u00F7", "0", "="), "Infinity")
                //"\u00F7" is UTF code for '÷' symbol
        );
    }
}


