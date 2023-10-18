package com.cst438;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EndToEndTestAssignments {
    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

    public static final String URL = "http://localhost:8081";
    public static final int SLEEP_DURATION = 1000; // 1 second.
    public static final String TEST_ASSIGNMENT_NAME = "db design";
    
    @Test
    public void createTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Create Assignment" button.
            driver.findElement(By.id("create")).click();
            Thread.sleep(SLEEP_DURATION);

            // Fill in assignment details and submit the form.
            driver.findElement(By.id("assignmentName")).sendKeys(TEST_ASSIGNMENT_NAME);
            driver.findElement(By.id("dueDate")).sendKeys("2023-12-31");
            driver.findElement(By.id("sedit")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the new assignment is created by searching for its name in the updated list.
            WebElement assignmentRow = driver.findElement(By.xpath("//tr[td='" + TEST_ASSIGNMENT_NAME + "']"));
            assertNotNull(assignmentRow, TEST_ASSIGNMENT_NAME + " not found in the assignment list.");
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
    
    @Test
    public void updateTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Edit Assignment" button for the assignment you want to update.
            driver.findElement(By.xpath("//Link [text()='Edit']")).click();
            Thread.sleep(SLEEP_DURATION);

            // Update assignment details here (e.g., due date or assignment name).
            driver.findElement(By.id("dueDate")).clear();
            driver.findElement(By.id("dueDate")).sendKeys("2023-12-31");
            driver.findElement(By.id("assignmentName")).clear();
            driver.findElement(By.id("assignmentName")).sendKeys("Updated Assignment Name");

            // Click the "Save" button to save the changes.
            driver.findElement(By.id("sedit")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the assignment details have been updated.
            WebElement updatedAssignmentName = driver.findElement(By.xpath("//tr[td='Updated Assignment Name']"));
            assertNotNull(updatedAssignmentName, "Updated assignment not found in the assignment list");
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
    
    @Test
    public void deleteTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Delete Assignment" button for the assignment you want to delete.
            driver.findElement(By.xpath("//button [text()='Delete']")).click();
            Thread.sleep(SLEEP_DURATION);

            // Check that the assignment is no longer in the assignment list.
            Thread.sleep(SLEEP_DURATION);
            assertThrows(NoSuchElementException.class, () -> {
                driver.findElement(By.xpath("//tr[td='Updated Assignment Name']"));
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
