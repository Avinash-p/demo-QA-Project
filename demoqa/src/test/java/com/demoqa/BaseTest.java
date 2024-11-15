package com.demoqa;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;

import com.aventstack.extentreports.Status;

@Listeners(ExtentReportListener.class)
public class BaseTest 
{
    WebDriver driver; 
    Map<String,String> testData;
    private ITestResult currentTestResult;

    @BeforeMethod
    public void afterMethod(ITestResult result) {
        this.currentTestResult = result;
    }

    /**
     * This method initializes the web driver, maximizes the browser window,
     * navigates to the specified URL, and sets the driver for the ExtentReportListener.
     */
    @BeforeClass
    public void start(){
        driver = new ChromeDriver(new ChromeOptions());
        driver.manage().window().maximize();
        driver.get("https://www.google.com/");
        ExtentReportListener.setDriver(driver);
    }

    /**
     * This method quits the web driver, closing the browser window.
     */
    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    /**
     * This method provides test data from an Excel file.
     * @return A Map containing test data, where keys are column headers and values are cell values.
     */
    @DataProvider(name = "Test Data Provider")
    public Map<String, String> getTestData(){
        Map<String,String> temp = new HashMap<String, String>();
        String dataFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData.xlsx";
        FileInputStream file;
        XSSFWorkbook excelFile;
        XSSFSheet excelSheet;
        try {
            dataFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData.xlsx";
            file = new FileInputStream(dataFilePath);
            excelFile = new XSSFWorkbook(file);
            excelSheet = excelFile.getSheetAt(0);
            XSSFRow currentRow = excelSheet.getRow(0);
            int lastRow = excelSheet.getLastRowNum();
            for (int i=0; i<=lastRow;i++){
                currentRow = excelSheet.getRow(i);
                temp.put(getValue(currentRow.getCell(0)), getValue(currentRow.getCell(1)));
            }
            file.close();
            return temp;
        } catch (Exception e) {
            reportLog("Exception occurred while loading test data: " + e.getMessage());
            failTest("Data provider failed due to: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void reportLog(String text){
        ExtentReportListener.log(currentTestResult, Status.INFO, text);
    }

    public void failTest(String reason){
        ExtentReportListener.fail(currentTestResult, reason);
    }

    /**
     * This method retrieves the value of an XSSFCell.
     * @param x The XSSFCell to retrieve the value from.
     * @return The value of the cell as a String.
     */
    public String getValue(XSSFCell x){
        if (x.getCellType() == CellType.NUMERIC){
            return Double.toString(x.getNumericCellValue());
        } else if (x.getCellType() == CellType.STRING){
            return x.getStringCellValue();
        }
        return null;
    }
    
    /**
     * This method enters text into a text area element on the web page.
     * @param x The text to be entered.
     */
}
