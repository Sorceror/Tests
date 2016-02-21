import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;

/**
 * Simple test of selenium web-driver
 * @author Pavel Janecka
 */
public class SearchInGoogleTest {
    private WebDriver webDriver;

    @Before
    public void openBrowser() {
        // windows and my machine specific code, comment out if not needed
        System.setProperty("webdriver.chrome.driver", "d:\\Workspace\\Tests\\Selenium\\chromedriver.exe");

        webDriver = new ChromeDriver();
        webDriver.get("http://www.google.com");
    }

    @After
    public void saveScreenshot() throws IOException {
        File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("test.screenshot.png"));
        webDriver.quit();
    }

    @Test
    public void pageTitleAfterSearchShouldBeginWithScalIO() throws InterruptedException {
        Assert.assertEquals("The page title should equals Google at the start of the test.", "Google", webDriver.getTitle());
        WebElement searchField = webDriver.findElement(By.name("q"));
        searchField.sendKeys("scal.io");
        searchField.submit();
        // wait for 3000 ms = 3 s
        Thread.sleep(3000);
        Assert.assertTrue("The search title should start with the search string after the search.",
                webDriver.getTitle().toLowerCase().startsWith("scal.io")
        );
    }
}
