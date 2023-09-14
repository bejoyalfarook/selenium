package com.webstaurant;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


public class WebstaurantTests {

    @Test
    public void tableTest() {
        System.out.println("Starting tableTest");

        // Open page
        String baseURL = "https://www.webstaurantstore.com/";
        WebDriver driver = new ChromeDriver();
        driver.get(baseURL);

        // Verify page is opened
        String expectedUrl = "https://www.webstaurantstore.com/";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "Actual page URL is not the same as expected");

        // Search for "stainless work table"
        WebElement searchBox = driver.findElement(By.id("searchval"));
        searchBox.sendKeys("stainless work table");

        WebElement searchButton = driver.findElement(By.xpath("//button[@value='Search']"));
        searchButton.click();
        

     // Initialize variables to keep track of the current page number and URL
        int currentPageNumber = 1;
        String currentUrl = driver.getCurrentUrl();

        // Loop until the current page number is 1 again (indicating the first page)
        while (true) {
        	// Locate and process search results on the current page
        	List<WebElement> searchResults = driver.findElements(By.id("product_listing"));
        	for (WebElement result : searchResults) {
        	    String title = result.findElement(By.cssSelector("span[data-testid='itemDescription']")).getText();
        	    if (!title.toLowerCase().contains("table")) {
        	        // Assertion failed, print the product name
        	        System.out.println("Assertion failed for product: " + title);
        	    }
        	    Assert.assertTrue(title.toLowerCase().contains("table"), "Title contains 'table'");
        	}

            // Check if we are on the last page or have gone back to the first page
            String newUrl = driver.getCurrentUrl();
            if (currentPageNumber == 1 && !newUrl.equals(currentUrl)) {
                // We have gone back to the first page; break out of the loop
                break;
            }

            // Guess the next page number
            int nextPageNumber = currentPageNumber + 1;

            // Construct the URL for the next page
            String nextPageUrl = "https://www.webstaurantstore.com/search/stainless-work-table.html?page=" + nextPageNumber;

            // Navigate to the next page
            driver.get(nextPageUrl);

            // Wait for page to load 
            try {
                Thread.sleep(2000); // Sleep for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update current page number and URL
            currentPageNumber = nextPageNumber;
            currentUrl = newUrl;
        }

        // Add the last product to the cart
        WebElement lastProductAddToCartButton = driver.findElement(By.name("addToCartButton"));
        lastProductAddToCartButton.click();

        // Wait for the toast/notification popup to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait for up to 10 seconds
        WebElement toastPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("notification__action")));

        // Locate and click the "View Cart" button inside the popup
        WebElement viewCartButton = toastPopup.findElement(By.linkText("View Cart"));
        viewCartButton.click();
        
        // Wait for page to load 
        try {
            Thread.sleep(2000); // Sleep for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Locate and click the "Empty Cart" button
        WebElement emptyCartButton = driver.findElement(By.cssSelector("button.emptyCartButton"));
        emptyCartButton.click();


        // Close the WebDriver when done
        driver.quit();
    }
}