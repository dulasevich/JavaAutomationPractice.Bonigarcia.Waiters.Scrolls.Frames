package browser.agnostic;

import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ShadowDomTest {
    private WebDriver driver;
    private final TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    private static final By SHADOW_ROOT_LOCATOR = By.cssSelector("p");

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driver.findElement(By.xpath("//a[@href='shadow-dom.html']")).click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void shadowDomNegativeTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(SHADOW_ROOT_LOCATOR));
    }

    @Test
    void shadowDomTest() {
        WebElement content = driver.findElement(By.xpath("//div[@id='content']"));
        SearchContext searchContext = content.getShadowRoot();
        WebElement shadowRootText = searchContext.findElement(SHADOW_ROOT_LOCATOR);

        Assertions.assertEquals("Hello Shadow DOM", shadowRootText.getText(), "Shadow dom text is incorrect");
    }
}
