package browser.agnostic;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ShadowDomTest {

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
    void shadowDomTest() {
        driver.navigate().to(BASE_URL + "/shadow-dom.html");
        By shadowRootLocator = By.cssSelector("p");
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(shadowRootLocator));

        WebElement content = driver.findElement(By.xpath("//div[@id='content']"));
        SearchContext searchContext = content.getShadowRoot();
        WebElement shadowRootText = searchContext.findElement(shadowRootLocator);
        Assertions.assertEquals("Hello Shadow DOM", shadowRootText.getText(), "Shadow dom text is incorrect");
    }
}
