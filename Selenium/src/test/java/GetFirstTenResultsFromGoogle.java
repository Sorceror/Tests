import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.List;

/**
 * Obtain first ten results from google when search from particular term and write them to console
 * @author Pavel Janecka
 */
public class GetFirstTenResultsFromGoogle {
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
        // find elements by XPath query -> tag a that has a parent h3 tag which has class set to 'r' and is located anywhere on the page
        // \" inside string (inside double quotes) is translated to '"' char
        // see https://www.wikiwand.com/en/XPath
        List<WebElement> resultLinks = webDriver.findElements(By.xpath("//h3[@class=\"r\"]/a"));
        Assert.assertEquals("There is exactly 11 results for 'scal.io' search query on the first page", 11, resultLinks.size());
        // this statement is called for-each, see http://www.tutorialspoint.com/javaexamples/method_for.htm
        for (WebElement link : resultLinks) {
            // print those results to the console
            System.out.println(link.getText());
        }
    }
}
