package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;

public class SearchResultsPage {
    WebDriver driver;
    WebDriverWait wait;

    By sortLowToHigh = By.xpath("//div[contains(text(),'Price -- Low to High')]");
    By productList = By.cssSelector("div[data-id]");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Screenshot helper for this class
    public void takeScreenshot(String name) {
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

    public void waitForResultsToLoad() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productList));
            System.out.println("-> Search results loaded successfully.");
        } catch (Exception e) {
            System.out.println("X Timeout waiting for results to load: " + e.getMessage());
        }
    }

    public void sortByLowToHigh() {
        try {
            WebElement sort = wait.until(ExpectedConditions.elementToBeClickable(sortLowToHigh));
            sort.click();
            System.out.println("-> Sorted by Price: Low to High");
            waitForResultsToLoad();
            takeScreenshot("AfterSorting");
        } catch (Exception e) {
            System.out.println("-> Sort option not found or failed: " + e.getMessage());
        }
    }

    public void selectBrand(String brandName) {
        try {
            WebElement brandDiv = driver.findElement(By.xpath("//div[text()='" + brandName + "']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", brandDiv);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", brandDiv);
            System.out.println("-> Selected brand: " + brandName);
            waitForResultsToLoad();
            takeScreenshot("AfterBrand_" + brandName);
        } catch (Exception e) {
            System.out.println("X Brand not found: " + brandName);
        }
    }

    public void selectRamFilter() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            WebElement ramSection = driver.findElement(By.xpath("//div[text()='RAM']"));
            js.executeScript("arguments[0].scrollIntoView(true);", ramSection);
            Thread.sleep(500);

            // Show More if exists
            try {
                WebElement showMore = driver.findElement(By.xpath("//div[text()='RAM']/following::span[text()='Show More']"));
                if (showMore.isDisplayed()) showMore.click();
                Thread.sleep(500);
            } catch (Exception ignored) {}

            boolean found = false;
            int attempts = 0;
            while (!found && attempts < 10) {
                List<WebElement> options = driver.findElements(By.xpath("//div[contains(text(),'GB')]"));
                for (WebElement option : options) {
                    if (option.getText().contains("8 GB")) {
                        WebElement checkbox = option.findElement(By.xpath("./preceding-sibling::div"));
                        js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
                        js.executeScript("arguments[0].click();", checkbox);
                        found = true;
                        System.out.println("-> Applied RAM filter: 8 GB & Above");
                        waitForResultsToLoad();
                        takeScreenshot("AfterRAMFilter");
                        break;
                    }
                }
                if (!found) {
                    js.executeScript("arguments[0].scrollTop += 100;", ramSection);
                    Thread.sleep(500);
                    attempts++;
                }
            }

            if (!found) System.out.println("X RAM filter not found");

        } catch (Exception e) {
            System.out.println("X Error applying RAM filter: " + e.getMessage());
        }
    }

    public void openFirstProduct() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productList));
            List<WebElement> products = driver.findElements(productList);

            if (!products.isEmpty()) {
                WebElement firstProduct = products.get(0).findElement(By.cssSelector("a"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProduct);
                Thread.sleep(500);
                firstProduct.click();
                System.out.println("-> Opened first product successfully.");
                takeScreenshot("AfterOpeningProduct");
            } else {
                System.out.println("-> No products found to select.");
            }
        } catch (Exception e) {
            System.out.println("X Failed to open first product: " + e.getMessage());
        }
    }
}
