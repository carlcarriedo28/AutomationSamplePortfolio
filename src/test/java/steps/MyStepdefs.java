/*
Company: Wefox
Technical Challenge - Software Developer in Test

Part 2: Automated testing
For this section, you have a choice between writing browser-based GUI tests, or API
tests. Attempt only one exercise.
Option 1: Browser testing
System under test:
https://demoqa.com/books

Here is the test case in Gherkin format:
Feature: Demoqa - Search Book
  Scenario: Successful searching of Programming JavaScript Applications book
    Given Testcase "tc1" from sheet "Testdata" Books list page is displayed
    When I search and click on the book title
    And I check the book details
    Then Go back to the Book Search Page
 */

package steps;

import engine.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page_objects.DemoqaBooksPage;
import page_objects.GetdatafromExcel;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MyStepdefs {
    GetdatafromExcel excelReader;
    DemoqaBooksPage qa = new DemoqaBooksPage();
    private WebDriver driver;
    private String title;
    private String isbn;
    private String author;

    //initialization of the driver
    public MyStepdefs (Driver driver){
        this.driver = driver.get();
    }

    //Navigate to the main page which displays books list and get testdata values from excel sheet
    @Given("Testcase {string} from sheet {string} Books list page is displayed")
    public void get_data_from_datasheet(String testId, String sheetName) {
        driver.get("https://demoqa.com/books");
        excelReader = new GetdatafromExcel(testId, sheetName);
        isbn = excelReader.fieldsAndValues.get("isbn");
        title = excelReader.fieldsAndValues.get("bookTitle");
        author = excelReader.fieldsAndValues.get("author");
    }

    //Search a book and click it
    @When("I search and click on the book title")
    public void searchBook() throws Exception {
        //declaration for WebDriverWait variable which is used in our explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Explicit wait - waits for the visibility of the search field element
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));

        //Enter the book title in the search field
        WebElement featuredProductDiv = driver.findElement(By.id("searchBox"));
        featuredProductDiv.sendKeys(excelReader.fieldsAndValues.get("bookTitle"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("see-book-Programming JavaScript Applications")));

        //Screenshot the page
        qa.takeSnapShot(driver, "booksearch");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Click on the found book
        driver.findElement(By.id("see-book-Programming JavaScript Applications")).click();
    }

    @And("I check the book details")
    public void theProductShouldBeAddedToTheShoppingCartSuccessfully() throws Exception {
        //declaration for WebDriverWait variable which is used in our explicit wait
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

        //Explicit wait - waits for the visibility of the element with id="userName-value"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userName-value")));

        //Assert the book details
        String isbnText = driver.findElement(By.id("ISBN-wrapper")).getText();
        Assert.assertTrue(isbnText.contains(isbn));
        String titleText = driver.findElement(By.id("title-wrapper")).getText();
        Assert.assertTrue(titleText.contains(title));
        String authorText = driver.findElement(By.id("author-wrapper")).getText();
        Assert.assertTrue(authorText.contains(author));

        //Take screenshot of book details page
        qa.takeSnapShot(driver, "bookdetails");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Then("Go back to the Book Search Page")
    public void backToSearchPage() throws Exception {
        //declaration for WebDriverWait variable which is used in our explicit wait
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

        //Scroll down the current page
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,200)", "");

        //Click Back To Book Store button
        driver.findElement(By.id("addNewRecordButton")).click();

        //Explicit wait - waits for the visibility of the element with id="searchBox"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));

        //Take screenshot of demoqabooks page
        qa.takeSnapShot(driver, "demoqabooks");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
   }
}