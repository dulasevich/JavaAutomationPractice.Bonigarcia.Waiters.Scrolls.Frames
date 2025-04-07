package browser.agnostic;

import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebStorageTest {
    private WebDriver driver;
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    private static final By LOCAL_STORAGE_LOCATOR = By.xpath("//p[@id='local-storage']");
    private static final By SESSION_STORAGE_LOCATOR = By.xpath("//p[@id='session-storage']");

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(config.getBaseUrl());
        driver.findElement(By.xpath("//a[@href='web-storage.html']")).click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void storageDefaultViewTest() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(driver.findElement(LOCAL_STORAGE_LOCATOR).getText().isEmpty(),
                        "Local storage is shown on default page"),
                () -> Assertions.assertTrue(driver.findElement(SESSION_STORAGE_LOCATOR).getText().isEmpty(),
                        "Session storage is shown on default page")
        );
    }

    @Test
    void showStorageTest() {
        Map<String, String> sessionStorageExpected = new LinkedHashMap<>();
        sessionStorageExpected.put("lastname", "Doe");
        sessionStorageExpected.put("name", "John");
        driver.findElement(By.xpath("//button[@id='display-local']")).click();
        driver.findElement(By.xpath("//button[@id='display-session']")).click();

        Map<String, String> localStorageMap = getLocalStorage(driver);
        Map<String, String> sessionStorageMap = getSessionStorage(driver);
        Assertions.assertAll(
                () -> Assertions.assertTrue(localStorageMap.isEmpty(), "Local storage is NOT empty"),
                () -> Assertions.assertEquals(sessionStorageExpected, sessionStorageMap,
                        "Session storage set in browser is wrong"),
                () -> Assertions.assertEquals("{}", driver.findElement(LOCAL_STORAGE_LOCATOR).getText(),
                        "Local storage displayed is incorrect"),
                () -> Assertions.assertEquals("{\"lastname\":\"Doe\",\"name\":\"John\"}", driver.findElement(SESSION_STORAGE_LOCATOR).getText())
        );
    }

    @Test
    void manageStorageTest() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("sessionStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('localKey', 'localValue');");
        ((JavascriptExecutor) driver).executeScript("sessionStorage.setItem('sessionKey', 'sessionValue');");

        driver.findElement(By.xpath("//button[@id='display-local']")).click();
        driver.findElement(By.xpath("//button[@id='display-session']")).click();

        Assertions.assertAll(
                () -> Assertions.assertEquals(getFormattedStorage("localKey", "localValue"), driver.findElement(LOCAL_STORAGE_LOCATOR).getText(),
                        "Local storage displayed is incorrect"),
                () -> Assertions.assertEquals(getFormattedStorage("sessionKey", "sessionValue"), driver.findElement(SESSION_STORAGE_LOCATOR).getText(),
                        "Session storage displayed is incorrect")
        );
    }

    private Map<String, String> getLocalStorage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String getLocalStorageScript = "var items = {}; " +
                "for (var i = 0; i < window.localStorage.length; i++) { " +
                "   var key = window.localStorage.key(i); " +
                "   items[key] = window.localStorage.getItem(key); " +
                "} " +
                "return items;";
        return (Map<String, String>) js.executeScript(getLocalStorageScript);
    }

    private Map<String, String> getSessionStorage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String getSessionStorageScript = "var items = {}; " +
                "for (var i = 0; i < window.sessionStorage.length; i++) { " +
                "   var key = window.sessionStorage.key(i); " +
                "   items[key] = window.sessionStorage.getItem(key); " +
                "} " +
                "return items;";
        return (Map<String, String>) js.executeScript(getSessionStorageScript);
    }

    private String getFormattedStorage(String key, String value) {
        return String.format("{\"%s\":\"%s\"}", key, value);
    }
}
