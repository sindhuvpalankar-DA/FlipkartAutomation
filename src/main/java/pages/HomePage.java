package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    WebDriver driver;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // ✅ Add this method to perform a search on Flipkart
    public void searchFor(String productName) {
        try {
            WebElement searchBox = driver.findElement(By.name("q")); // Flipkart's search box
            searchBox.clear();
            searchBox.sendKeys(productName);
            searchBox.submit();
            System.out.println("-> Searched for: " + productName);
        } catch (Exception e) {
            System.out.println("X Error while searching: " + e.getMessage());
        }
    }
}
