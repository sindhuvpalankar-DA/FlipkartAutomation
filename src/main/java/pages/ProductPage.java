package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {
    WebDriver driver;
    WebDriverWait wait;

    By addToCartBtn = By.xpath("//button[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add to cart')]");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void addToCart() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
            button.click();
            System.out.println("-> Product added to cart successfully.");
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(addToCartBtn));
            System.out.println("-> Product added to cart successfully via JS click.");
        } catch (Exception e) {
            System.out.println("X Failed to add product to cart: " + e.getMessage());
        }
    }
}
