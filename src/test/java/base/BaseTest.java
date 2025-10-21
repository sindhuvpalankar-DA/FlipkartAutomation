package base;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.nio.file.Files;

public class BaseTest {
    protected WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.out.println("Starting Chrome Browser...");

        // ✅ Specify your local ChromeDriver path manually
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\sindh\\OneDrive\\Desktop\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        System.out.println("Chrome launched successfully.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }

    // ✅ Wait helper
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ✅ Screenshot helper
    protected void takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File dest = new File("screenshots/" + name + "_" + timestamp + ".png");
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath());
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Popup handling (for Flipkart login popup)
    protected void handlePopupIfPresent() {
        try {
            By popupClose = By.cssSelector("button._2KpZ6l._2doB4z");
            WebElement closeBtn = driver.findElement(popupClose);
            if (closeBtn.isDisplayed()) {
                closeBtn.click();
                System.out.println("Closed login popup.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("No popup appeared");
        }
    }
}
