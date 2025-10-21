package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;
import org.testng.annotations.Test;

public class ProductWorkflowTest extends BaseTest {

    @Test
    public void testProductWorkflow() throws InterruptedException {
        System.out.println("===== Starting Flipkart Workflow Test =====");

        // Open Flipkart
        driver.get("https://www.flipkart.com");
        handlePopupIfPresent();
        takeScreenshot("HomePage");

        // Search for product
        HomePage home = new HomePage(driver);
        home.searchFor("phone");
        waitForSeconds(1); // short wait to ensure page loads
        takeScreenshot("SearchResults");

        SearchResultsPage results = new SearchResultsPage(driver);

        // Sort results
        results.sortByLowToHigh();
        waitForSeconds(1);
        takeScreenshot("SortedResults");

        // Select brands
        results.selectBrand("Apple");
        waitForSeconds(1);
        takeScreenshot("Brand_Apple");

        results.selectBrand("Google");
        waitForSeconds(1);
        takeScreenshot("Brand_Google");

        results.selectBrand("vivo");
        waitForSeconds(1);
        takeScreenshot("Brand_Vivo");

        // Apply RAM filter
        results.selectRamFilter();
        waitForSeconds(1);
        takeScreenshot("RAM_FilteredResults");

        // Open first product
        String mainTab = driver.getWindowHandle();
        results.openFirstProduct();
        waitForSeconds(1);
        takeScreenshot("FirstProduct");

        // Switch to product tab
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainTab)) {
                driver.switchTo().window(handle);
                System.out.println("Switched to product tab");
                break;
            }
        }

        takeScreenshot("ProductPage");
        waitForSeconds(1);

        // Add product to cart
        ProductPage product = new ProductPage(driver);
        product.addToCart();
        waitForSeconds(1);
        takeScreenshot("CartPage");

        // Open cart page
        driver.get("https://www.flipkart.com/viewcart");
        waitForSeconds(2); // wait for cart to load
        takeScreenshot("FinalCart");
        System.out.println("-> Cart opened successfully");

        // Wait 2 seconds on cart page
        waitForSeconds(2);

        // Close browser
        driver.quit();
        System.out.println("Browser closed successfully.");
    }
}
