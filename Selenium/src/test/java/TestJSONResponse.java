import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarContent;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

/**
 * Simple test that points out how to intercept AJAX - JSON response after form is submitted on the page (and triggers JSON response)
 * @author Pavel Janecka
 */
public class TestJSONResponse {
    private WebDriver webDriver;
    private BrowserMobProxy proxy;

    @Before
    public void openBrowser() {
        // windows and my machine specific code, comment out if not needed
        System.setProperty("webdriver.chrome.driver", "d:\\Workspace\\Tests\\Selenium\\chromedriver.exe");

        // create proxy server that will allow to intercept communication between client and server
        proxy = new BrowserMobProxyServer();
        // setup the proxy to capture actual content (text) of responses (disabled on default)
        proxy.setHarCaptureTypes(CaptureType.RESPONSE_CONTENT);
        proxy.start();

        // setup proxy to selenium framework
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        // create web driver with selected proxy
        webDriver = new ChromeDriver(capabilities);
        webDriver.get("http://labs.jonsuh.com/jquery-ajax-php-json/");
    }

    @After
    public void saveScreenshot() throws IOException {
        webDriver.quit();
        proxy.stop();
    }

    @Test
    public void fillFormAndTestJSONResponse() throws InterruptedException {
        // obtained from chrome network inspector
        // because " character means beginning and end of string, it has to be escaped to have regular double quote meaning \" -> "
        // because \ has also special meaning has to be escaped as well \\ -> \
        // \\\"action\\\" then really means only \"action\"
        // if string copied from some text and paste inside "" in Intellij IDEA it's automatically escaped
        String expectedJSONResponseContent = "{\"favorite_restaurant\":\"sushi place\",\"favorite_beverage\":\"green tea\",\"gender\":\"male\",\"action\":\"test\",\"json\":\"{\\\"favorite_restaurant\\\":\\\"sushi place\\\",\\\"favorite_beverage\\\":\\\"green tea\\\",\\\"gender\\\":\\\"male\\\",\\\"action\\\":\\\"test\\\"}\"}";

        // check correct page is loaded
        Assert.assertEquals("Proper page loaded", "Demo: jQuery Ajax Call to PHP Script with JSON Return — Jonathan Suh Labs", webDriver.getTitle());

        // start collecting request/response data going through proxy with selected identifier
        proxy.newHar("responsetest");

        // fill out form and submit it
        WebElement favoriteRestaurant = webDriver.findElement(By.name("favorite_restaurant"));
        favoriteRestaurant.sendKeys("sushi place");
        WebElement favoriteBeverage = webDriver.findElement(By.name("favorite_beverage"));
        favoriteBeverage.sendKeys("green tea");
        favoriteBeverage.submit();

        // wait for the response 3 seconds
        Thread.sleep(3000);

        // collect all captured data from proxy
        Har har = proxy.getHar();
        // check if anything captured at all
        Assert.assertTrue("There are some entries in HAR", !har.getLog().getEntries().isEmpty());
        // get first of collected request and get content of response
        HarContent content = har.getLog().getEntries().get(0).getResponse().getContent();
        // check there is any content of first response
        Assert.assertNotNull("There is some content in first entry", content);
        // check that mime type is as expected
        Assert.assertEquals("Expect correct mime type", "text/html", content.getMimeType());
        // check that response is as expected
        Assert.assertEquals("Expected JSON response content", expectedJSONResponseContent, content.getText());
    }
}
